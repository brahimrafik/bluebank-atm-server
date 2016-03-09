package org.bluebank.atm.transaction.outbound;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.bluebank.api.domain.PlatformClock;
import org.bluebank.api.endpoint.OutboundEndPoint;
import org.bluebank.atm.Atm;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.Transaction.WithdrawRequested;
import org.bluebank.banking.Bank;
import org.bluebank.contract.Messages.Receipt;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.LocalDateTime;

import static com.google.common.collect.ImmutableList.of;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;

@Singleton
public class WithdrawRequestedHandler {
    private final OutboundEndPoint<Receipt> receiptSender;
    private final Atm atm;
    private final Bank bank;
    private final PlatformClock platformClock;

    @Inject
    public WithdrawRequestedHandler(EventBus eventBus,
                                    @Named("receiptSender") OutboundEndPoint<Receipt> receiptSender,
                                    Atm atm,
                                    Bank bank,
                                    PlatformClock platformClock) {
        this.receiptSender = receiptSender;
        this.atm = atm;
        this.bank = bank;
        this.platformClock = platformClock;
        eventBus.register(this);
    }

    @Subscribe
    public void requestWithdraw(WithdrawRequested withdrawRequested) {
        atm.dispenseCash(withdrawRequested.amount);
        bank.performWithdraw(withdrawRequested.id, withdrawRequested.cardNumber, withdrawRequested.amount);
    }

    @Subscribe
    public void forwardDepositReceipt(Transaction.WithdrawPerformed withdrawPerformed) {
        atm.ejectCard();
        LocalDateTime date = platformClock.getDate();
        receiptSender.send(Receipt.newBuilder()
                        .addAllData(of(
                                format("LOCATION: %s", withdrawPerformed.atmInfo.location),
                                format("CARD NO: %s", withdrawPerformed.cardNumber),
                                format(
                                        "DATE %s TIME %s",
                                        date.toLocalDate().format(ofPattern("dd/MM/yyyy")),
                                        date.toLocalTime().format(ofPattern("HH:mm:ss"))
                                ),
                                format("TERMINAL %s", withdrawPerformed.atmInfo.id),
                                format("TRANSACTION#%s", withdrawPerformed.transactionId),
                                format("WITHDRAW OF $%s", withdrawPerformed.amount)
                        ))
                        .build()
        );
    }
}

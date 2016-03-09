package org.bluebank.atm.transaction.outbound;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.bluebank.api.domain.PlatformClock;
import org.bluebank.api.endpoint.OutboundEndPoint;
import org.bluebank.atm.Atm;
import org.bluebank.banking.Bank;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.LocalDateTime;

import static com.google.common.collect.ImmutableList.of;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.bluebank.atm.Transaction.Inquired;
import static org.bluebank.atm.Transaction.InquiryRequested;
import static org.bluebank.contract.Messages.Receipt;

@Singleton
public class InquiryRequestedHandler {
    private final OutboundEndPoint<Receipt> receiptSender;
    private final Atm atm;
    private final Bank bank;
    private final PlatformClock platformClock;

    @Inject
    public InquiryRequestedHandler(EventBus eventBus,
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
    public void requestInquiry(InquiryRequested inquiryRequested) {
        bank.performInquiry(inquiryRequested.id, inquiryRequested.accountNumber);
    }

    @Subscribe
    public void forwardInquiryReceipt(Inquired inquired) {
        atm.ejectCard();
        LocalDateTime date = platformClock.getDate();
        receiptSender.send(Receipt.newBuilder()
                        .addAllData(of(
                                format("LOCATION: %s", inquired.atmInfo.location),
                                format("CARD NO: %s", inquired.accountNumber),
                                format(
                                        "DATE %s TIME %s",
                                        date.toLocalDate().format(ofPattern("dd/MM/yyyy")),
                                        date.toLocalTime().format(ofPattern("HH:mm:ss"))),
                                format("TERMINAL %s", inquired.atmInfo.id),
                                format("BALANCE $%s", inquired.balance)
                        ))
                        .build()
        );
    }
}

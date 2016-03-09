package org.bluebank.banking.transaction.outbound;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.banking.account.model.Account;
import org.bluebank.banking.transaction.model.TransactionConfirmation;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class WithdrawPerformedHandler {
    private final InboundEndPoint<TransactionConfirmation> withdrawResponseReceiver;

    @Inject
    public WithdrawPerformedHandler(EventBus eventBus,
                                    @Named("withdrawResponseReceiver")
                                    InboundEndPoint<TransactionConfirmation> withdrawResponseReceiver) {
        this.withdrawResponseReceiver = withdrawResponseReceiver;
        eventBus.register(this);
    }

    @Subscribe
    public void forwardWithdrawPerformed(Account.WithdrawPerformed withdrawPerformed) {
        TransactionConfirmation transactionConfirmation = new TransactionConfirmation(
                withdrawPerformed.atmTransaction,
                withdrawPerformed.amount,
                withdrawPerformed.cardNumber,
                withdrawPerformed.transactionId
        );
        withdrawResponseReceiver.handle(transactionConfirmation);
    }
}

package org.bluebank.banking.transaction.outbound;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.banking.transaction.model.TransactionConfirmation;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static org.bluebank.banking.account.model.Account.DepositPerformed;

@Singleton
public class DepositPerformedHandler {
    private final InboundEndPoint<TransactionConfirmation> depositResponseReceiver;

    @Inject
    public DepositPerformedHandler(EventBus eventBus,
                                   @Named("depositResponseReceiver")
                                   InboundEndPoint<TransactionConfirmation> depositResponseReceiver) {
        this.depositResponseReceiver = depositResponseReceiver;
        eventBus.register(this);
    }

    @Subscribe
    public void forwardDespositPerformed(DepositPerformed depositPerformed) {
        TransactionConfirmation transactionConfirmation = new TransactionConfirmation(
                depositPerformed.atmTransaction,
                depositPerformed.amount,
                depositPerformed.cardNumber,
                depositPerformed.transactionId
        );
        depositResponseReceiver.handle(transactionConfirmation);
    }
}

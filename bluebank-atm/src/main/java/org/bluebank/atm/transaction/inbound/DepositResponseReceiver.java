package org.bluebank.atm.transaction.inbound;


import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.transaction.command.HandleDepositResponse;
import org.bluebank.atm.transaction.command.factory.HandleDepositResponseFactory;
import org.bluebank.banking.transaction.model.TransactionConfirmation;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DepositResponseReceiver implements InboundEndPoint<TransactionConfirmation> {
    private final HandleDepositResponseFactory handleDepositResponseFactory;
    private final CommandProcessor commandProcessor;

    @Inject
    public DepositResponseReceiver(HandleDepositResponseFactory handleDepositResponseFactory,
                                   CommandProcessor CommandProcessor) {
        this.handleDepositResponseFactory = handleDepositResponseFactory;
        this.commandProcessor = CommandProcessor;
    }

    @Override
    public void handle(TransactionConfirmation transactionConfirmation) {
        HandleDepositResponse handleDepositResponse = handleDepositResponseFactory.build(
                transactionConfirmation.id,
                transactionConfirmation.amount,
                transactionConfirmation.cardNumber,
                transactionConfirmation.transactionId
        );
        commandProcessor.process(handleDepositResponse);
    }
}

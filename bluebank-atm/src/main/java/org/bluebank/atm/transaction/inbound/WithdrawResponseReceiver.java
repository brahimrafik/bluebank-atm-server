package org.bluebank.atm.transaction.inbound;


import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.transaction.command.HandleWithdrawResponse;
import org.bluebank.atm.transaction.command.factory.HandleWithdrawResponseFactory;
import org.bluebank.banking.transaction.model.TransactionConfirmation;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WithdrawResponseReceiver implements InboundEndPoint<TransactionConfirmation> {
    private final HandleWithdrawResponseFactory handleWithdrawResponseFactory;
    private final CommandProcessor commandProcessor;

    @Inject
    public WithdrawResponseReceiver(HandleWithdrawResponseFactory handleWithdrawResponseFactory,
                                    CommandProcessor CommandProcessor) {
        this.handleWithdrawResponseFactory = handleWithdrawResponseFactory;
        this.commandProcessor = CommandProcessor;
    }

    @Override
    public void handle(TransactionConfirmation transactionConfirmation) {
        HandleWithdrawResponse handleWithdrawResponse = handleWithdrawResponseFactory.build(
                transactionConfirmation.id,
                transactionConfirmation.amount,
                transactionConfirmation.cardNumber,
                transactionConfirmation.transactionId
        );
        commandProcessor.process(handleWithdrawResponse);
    }
}

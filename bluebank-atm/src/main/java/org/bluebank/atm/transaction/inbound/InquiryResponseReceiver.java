package org.bluebank.atm.transaction.inbound;

import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.transaction.command.HandleInquiryResponse;
import org.bluebank.atm.transaction.command.factory.HandleInquiryResponseFactory;
import org.bluebank.banking.transaction.model.Inquiry;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InquiryResponseReceiver implements InboundEndPoint<Inquiry> {
    private final HandleInquiryResponseFactory handleInquiryResponseFactory;
    private final CommandProcessor commandProcessor;

    @Inject
    public InquiryResponseReceiver(HandleInquiryResponseFactory handleInquiryResponseFactory,
                                   CommandProcessor commandProcessor) {
        this.handleInquiryResponseFactory = handleInquiryResponseFactory;
        this.commandProcessor = commandProcessor;
    }

    @Override
    public void handle(Inquiry inquiry) {
        HandleInquiryResponse handleInquiryResponse = handleInquiryResponseFactory.build(
                inquiry.id,
                inquiry.balance,
                inquiry.cardNumber
        );
        commandProcessor.process(handleInquiryResponse);
    }
}

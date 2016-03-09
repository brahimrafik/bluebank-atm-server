package org.bluebank.atm.transaction.inbound;

import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.transaction.command.RequestInquiry;
import org.bluebank.atm.transaction.command.factory.RequestInquiryFactory;
import org.bluebank.contract.Messages.InquiryRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.UUID.fromString;

@Singleton
public class InquiryRequestReceiver implements InboundEndPoint<InquiryRequest> {
    private final RequestInquiryFactory requestInquiryFactory;
    private final CommandProcessor commandController;

    @Inject
    public InquiryRequestReceiver(RequestInquiryFactory requestInquiryFactory,
                                  CommandProcessor commandController) {
        this.requestInquiryFactory = requestInquiryFactory;
        this.commandController = commandController;
    }

    @Override
    public void handle(InquiryRequest inquiryRequest) {
        RequestInquiry requestInquiry = requestInquiryFactory.build(fromString(inquiryRequest.getTransactionId()));
        commandController.process(requestInquiry);
    }
}

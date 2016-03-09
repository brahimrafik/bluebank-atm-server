package org.bluebank.atm.transaction.outbound;

import org.bluebank.api.endpoint.OutboundEndPoint;
import org.bluebank.atm.Message;
import org.bluebank.resource.AtmResource;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.bluebank.atm.Message.EventType.RECEIPT;
import static org.bluebank.contract.Messages.Receipt;

@Singleton
public class ReceiptSender implements OutboundEndPoint<Receipt> {
    private final AtmResource serverEndpoint;

    @Inject
    public ReceiptSender(AtmResource serverEndpoint) {
        this.serverEndpoint = serverEndpoint;
    }

    @Override
    public void send(Receipt receipt) {
        Message message = new Message(RECEIPT, receipt.toByteArray());
        serverEndpoint.send(message);
    }
}

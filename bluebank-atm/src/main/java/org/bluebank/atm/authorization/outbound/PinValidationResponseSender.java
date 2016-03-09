package org.bluebank.atm.authorization.outbound;

import org.bluebank.api.endpoint.OutboundEndPoint;
import org.bluebank.atm.Message;
import org.bluebank.contract.Messages.CardValidationStatus;
import org.bluebank.resource.AtmResource;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.bluebank.atm.Message.EventType.CARD_VALIDATION_STATUS;

@Singleton
public class PinValidationResponseSender implements OutboundEndPoint<CardValidationStatus> {
    private final AtmResource serverEndpoint;

    @Inject
    public PinValidationResponseSender(AtmResource serverEndpoint) {
        this.serverEndpoint = serverEndpoint;
    }

    @Override
    public void send(CardValidationStatus cardValidationStatus) {
        Message message = new Message(CARD_VALIDATION_STATUS, cardValidationStatus.toByteArray());
        serverEndpoint.send(message);
    }
}

package org.bluebank.atm.authorization.outbound;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.bluebank.api.endpoint.OutboundEndPoint;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.Atm;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static org.bluebank.contract.Messages.CardValidationStatus;
import static org.bluebank.contract.Messages.CardValidationStatus.Status.CARD_DISABLED;
import static org.bluebank.contract.Messages.CardValidationStatus.Status.VALIDATED;
import static org.bluebank.contract.Messages.CardValidationStatus.Status.WRONG_PIN;

@Singleton
public class PinValidationResponseHandler {
    private final OutboundEndPoint<CardValidationStatus> pinValidationResponseSender;
    private final Atm atm;

    @Inject
    public PinValidationResponseHandler(EventBus eventBus,
                                        @Named("pinValidationResponseSender")
                                        OutboundEndPoint<CardValidationStatus> pinValidationResponseSender,
                                        Atm atm) {
        this.pinValidationResponseSender = pinValidationResponseSender;
        this.atm = atm;
        eventBus.register(this);
    }

    @Subscribe
    public void handlePinValidResponse(Transaction.PinValidated pinValidated) {
        CardValidationStatus cardValidationStatus = CardValidationStatus.newBuilder()
                .setStatus(VALIDATED)
                .build();
        pinValidationResponseSender.send(cardValidationStatus);
    }

    @Subscribe
    public void handleCardRetainedResponse(Transaction.CardRetained cardRetained) {
        atm.retainCard();
        CardValidationStatus cardValidationStatus = CardValidationStatus.newBuilder()
                .setReason("Card has been retained")
                .setStatus(CARD_DISABLED)
                .build();
        pinValidationResponseSender.send(cardValidationStatus);
    }

    @Subscribe
    public void handlePinInvalidResponse(Transaction.PinInvalid pinInvalid) {
        CardValidationStatus cardValidationStatus = CardValidationStatus.newBuilder()
                .setStatus(WRONG_PIN)
                .setReason("Wrong PIN")
                .build();
        pinValidationResponseSender.send(cardValidationStatus);
    }

}

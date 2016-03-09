package org.bluebank.banking.authorization.outbound;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.bluebank.api.endpoint.InboundEndPoint;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static org.bluebank.banking.account.model.Account.CardDisabled;
import static org.bluebank.banking.account.model.Account.PinValidated;
import static org.bluebank.banking.account.model.Account.PinValidationFailed;
import static org.bluebank.banking.authorization.outbound.PinValidationResponse.ValidationStatus.DISABLED;
import static org.bluebank.banking.authorization.outbound.PinValidationResponse.ValidationStatus.VALID;
import static org.bluebank.banking.authorization.outbound.PinValidationResponse.ValidationStatus.WRONG_PIN;

@Singleton
public class PinValidationHandler {
    private final InboundEndPoint<PinValidationResponse> pinValidationResponseReceiver;

    @Inject
    public PinValidationHandler(EventBus eventBus,
                                @Named("pinValidationResponseReceiver")
                                InboundEndPoint<PinValidationResponse> pinValidationResponseReceiver) {
        this.pinValidationResponseReceiver = pinValidationResponseReceiver;
        eventBus.register(this);
    }

    @Subscribe
    public void forwardPinValidated(PinValidated pinValidated) {
        PinValidationResponse pinValidationResponse = new PinValidationResponse(pinValidated.atmTransaction, VALID);
        pinValidationResponseReceiver.handle(pinValidationResponse);
    }

    @Subscribe
    public void forwardCardDisabled(CardDisabled cardDisabled) {
        PinValidationResponse pinValidationResponse = new PinValidationResponse(cardDisabled.atmTransaction, DISABLED);
        pinValidationResponseReceiver.handle(pinValidationResponse);
    }

    @Subscribe
    public void forwardPinValidationFailed(PinValidationFailed pinValidationFailed) {
        PinValidationResponse pinValidationResponse = new PinValidationResponse(
                pinValidationFailed.atmTransaction,
                WRONG_PIN
        );
        pinValidationResponseReceiver.handle(pinValidationResponse);
    }
}

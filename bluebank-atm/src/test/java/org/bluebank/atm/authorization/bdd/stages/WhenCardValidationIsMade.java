package org.bluebank.atm.authorization.bdd.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.Quoted;
import com.tngtech.jgiven.annotation.Table;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.bdd.BddComponent;
import org.bluebank.bdd.model.Card;

import static java.util.Arrays.stream;
import static org.bluebank.contract.Messages.ValidateCardRequest;
import static org.bluebank.contract.Messages.ValidatePinRequest;

public class WhenCardValidationIsMade<SELF extends WhenCardValidationIsMade<?>> extends Stage<SELF> {

    @ExpectedScenarioState
    private BddComponent bddComponent;

    @ExpectedScenarioState
    private ValidateCardRequest validateCardRequest;

    private InboundEndPoint<ValidatePinRequest> validatePinRequestReceiver;

    @BeforeStage
    public void before() {
        validatePinRequestReceiver = bddComponent.getValidatePinRequestReceiver();
    }

    public SELF the_wrong_pin_number_$_is_entered(@Quoted String pinNumber) {
        ValidatePinRequest validationRequest = ValidatePinRequest.newBuilder()
                .setTransactionId(validateCardRequest.getTransactionId())
                .setPin(pinNumber)
                .build();
        validatePinRequestReceiver.handle(validationRequest);
        return self();
    }

    public SELF wrong_pins_numbers_are_entered_three_times_in_a_row(@Table(columnTitles = "Pin Number") Card... cards) {
        stream(cards).forEach(card -> {
            ValidatePinRequest validationRequest = ValidatePinRequest.newBuilder()
                    .setTransactionId(validateCardRequest.getTransactionId())
                    .setPin(card.pinNumber)
                    .build();
            validatePinRequestReceiver.handle(validationRequest);
        });
        return self();
    }

}

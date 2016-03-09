package org.bluebank.atm.transaction.bdd.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.bluebank.api.domain.IdGenerator;
import org.bluebank.api.domain.PlatformClock;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.Atm;
import org.bluebank.atm.authorization.model.Card;
import org.bluebank.bdd.BddComponent;
import org.mockito.Mockito;

import java.util.UUID;

import static java.time.LocalDateTime.of;
import static java.time.Month.JULY;
import static org.bluebank.atm.CardBuilder.aDefaultCard;
import static org.bluebank.atm.ValidateCardRequestBuilder.aDefaultValidateCardRequest;
import static org.bluebank.contract.Messages.ValidateCardRequest;
import static org.bluebank.contract.Messages.ValidatePinRequest;

public class GivenAnAtm<SELF extends GivenAnAtm<?>> extends Stage<SELF> {

    @ExpectedScenarioState
    private BddComponent bddComponent;

    @ExpectedScenarioState
    private ValidateCardRequest validateCardRequest;

    @ExpectedScenarioState
    private Card card = aDefaultCard().build();

    @ExpectedScenarioState
    private String pin;

    private InboundEndPoint<ValidatePinRequest> validatePinRequestReceiver;
    private InboundEndPoint<ValidateCardRequest> validateCardRequestReceiver;
    private Atm atm;

    @BeforeStage
    public void before() {
        bddComponent.getCardValidationRequestedHandler();
        bddComponent.getPinValidationResponseHandler();
        bddComponent.getDepositRequestedHandler();
        bddComponent.getWithdrawRequestedHandler();
        bddComponent.getInquiryRequestedHandler();
        bddComponent.getPinValidationRequestHandler();
        validatePinRequestReceiver = bddComponent.getValidatePinRequestReceiver();
        validateCardRequestReceiver = bddComponent.getValidateCardRequestReceiver();
        atm = bddComponent.getAtm();
        IdGenerator idGenerator = bddComponent.getIdGenerator();
        PlatformClock platformClock = bddComponent.getPlatformClock();
        Mockito.when(idGenerator.generate()).thenReturn(new UUID(1, 2));
        Mockito.when(platformClock.getDate()).thenReturn(of(2015, JULY, 1, 17, 5));
    }

    public SELF the_card_is_inserted() {
        Mockito.when(atm.readCard(card.cardNumber)).thenReturn(card);

        validateCardRequest = aDefaultValidateCardRequest()
                .withCardNumber(card.cardNumber)
                .build();

        validateCardRequestReceiver.handle(validateCardRequest);
        return self();
    }

    public SELF the_valid_pin_number_has_been_entered() {
        the_card_is_inserted();
        validatePinRequestReceiver.handle(ValidatePinRequest.newBuilder()
                .setTransactionId(validateCardRequest.getTransactionId())
                .setPin(pin)
                .build());
        return self();
    }

}

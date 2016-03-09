package org.bluebank.atm.authorization.bdd.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.Quoted;
import org.bluebank.api.endpoint.OutboundEndPoint;
import org.bluebank.atm.Atm;
import org.bluebank.bdd.BddComponent;
import org.bluebank.contract.Messages.CardValidationStatus;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ThenAtmRejectTransaction<SELF extends ThenAtmRejectTransaction<?>> extends Stage<SELF> {

    @ExpectedScenarioState
    private BddComponent bddComponent;

    private ArgumentCaptor<CardValidationStatus> captor;
    private OutboundEndPoint<CardValidationStatus> cardValidationStatusSender;
    private Atm atm;

    @BeforeStage
    public void before() {
        cardValidationStatusSender = bddComponent.getPinValidationResponseSender();
        captor = ArgumentCaptor.forClass(CardValidationStatus.class);
        atm = bddComponent.getAtm();
    }

    public SELF the_atm_should_displayed_the_message(@Quoted String message) {
        verify(cardValidationStatusSender).send(captor.capture());
        CardValidationStatus values = captor.getValue();
        assertThat(values.getReason()).isEqualTo(message);
        return self();
    }

    public SELF the_atm_should_retained_the_card_and_displayed_the_message(@Quoted String message) {
        verify(atm).retainCard();
        verify(cardValidationStatusSender, times(3)).send(captor.capture());
        List<CardValidationStatus> values = captor.getAllValues();
        assertThat(values.get(2).getReason()).isEqualTo(message);
        return self();
    }
}

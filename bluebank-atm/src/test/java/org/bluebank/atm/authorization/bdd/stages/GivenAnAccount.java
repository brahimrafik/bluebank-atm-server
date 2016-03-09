package org.bluebank.atm.authorization.bdd.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.Quoted;
import org.bluebank.atm.authorization.model.Card;
import org.bluebank.banking.Bank;
import org.bluebank.bdd.BddComponent;

import java.math.BigDecimal;
import java.util.UUID;

import static org.bluebank.atm.CardBuilder.aDefaultCard;

public class GivenAnAccount<SELF extends GivenAnAccount<?>> extends Stage<SELF> {

    @ExpectedScenarioState
    private BddComponent bddComponent;

    @ProvidedScenarioState
    private Card card = aDefaultCard().build();

    @ProvidedScenarioState
    private String pin  = "1234";

    @ProvidedScenarioState
    private UUID accountId  = new UUID(0, 1);

    private Bank bank;

    @BeforeStage
    public void before() {
        bank = bddComponent.getBank();
        bddComponent.getDepositPerformedHandler();
        bddComponent.getWithdrawPerformedHandler();
        bddComponent.getInquiryPerformedHandler();
        bddComponent.getPinValidationHandler();
    }

    public SELF a_card_with_a_pin_number_of_$(@Quoted String pinNumber) {
        bank.createAccount(accountId, card.cardNumber, pinNumber);
        return self();
    }

    public SELF the_account_balance_is_$_dollars(String amount) {
        bank.createAccount(accountId, card.cardNumber, pin);
        bank.performDeposit(new UUID(0, 2), card.cardNumber, new BigDecimal(amount));
        return self();
    }
}

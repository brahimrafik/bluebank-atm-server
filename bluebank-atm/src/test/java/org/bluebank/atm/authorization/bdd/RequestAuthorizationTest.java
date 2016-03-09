package org.bluebank.atm.authorization.bdd;

import com.tngtech.jgiven.annotation.ScenarioStage;
import org.bluebank.atm.authorization.bdd.stages.GivenAnAccount;
import org.bluebank.atm.transaction.bdd.stages.GivenAnAtm;
import org.bluebank.atm.authorization.bdd.stages.ThenAtmRejectTransaction;
import org.bluebank.atm.authorization.bdd.stages.WhenCardValidationIsMade;
import org.bluebank.bdd.BddScenarioTest;
import org.bluebank.bdd.model.Card;
import org.bluebank.bdd.tags.Authorization;
import org.junit.Test;

@Authorization
public class RequestAuthorizationTest extends
        BddScenarioTest<GivenAnAccount<?>, WhenCardValidationIsMade<?>, ThenAtmRejectTransaction<?>> {

    @ScenarioStage
    private GivenAnAtm<?> $;

    @Test
    public void customer_enters_a_wrong_pin_number() {

        given().a_card_with_a_pin_number_of_$("0000");
                $.and().the_card_is_inserted();

        when().the_wrong_pin_number_$_is_entered("1234");

        then().the_atm_should_displayed_the_message("Wrong PIN");
    }

    @Test
    public void customer_enters_wrong_pins_numbers_three_times_in_a_row() {

        given().a_card_with_a_pin_number_of_$("0000");
                $.and().the_card_is_inserted();

        when().wrong_pins_numbers_are_entered_three_times_in_a_row(
                new Card("1234"),
                new Card("2134"),
                new Card("1000")
        );

        then().the_atm_should_retained_the_card_and_displayed_the_message("Card has been retained");
    }
}

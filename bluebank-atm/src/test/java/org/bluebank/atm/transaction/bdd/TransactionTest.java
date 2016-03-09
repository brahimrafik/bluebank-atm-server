package org.bluebank.atm.transaction.bdd;

import com.tngtech.jgiven.annotation.ScenarioStage;
import org.bluebank.atm.authorization.bdd.stages.GivenAnAccount;
import org.bluebank.atm.transaction.bdd.stages.GivenAnAtm;
import org.bluebank.atm.transaction.bdd.stages.ThenAtmDispenseMoney;
import org.bluebank.atm.transaction.bdd.stages.WhenTransactionIsMade;
import org.bluebank.bdd.BddScenarioTest;
import org.bluebank.bdd.model.Receipt;
import org.bluebank.bdd.tags.Deposit;
import org.bluebank.bdd.tags.Withdraw;
import org.junit.Test;

public class TransactionTest extends
        BddScenarioTest<GivenAnAccount<?>, WhenTransactionIsMade<?>, ThenAtmDispenseMoney<?>> {

    @ScenarioStage
    private GivenAnAtm<?> $;

    @Test
    @Deposit
    public void deposit_cash_into_account() {
        given().the_account_balance_is_$_dollars("300");
                $.and().the_valid_pin_number_has_been_entered();

        when().a_deposit_of_$_dollars_is_made("20.00");

        then().the_ATM_accepts_$_dollars("20.00")
                .and().the_account_balance_is_$_dollars("320.00")
                .and().the_card_is_returned()
                .and().a_receipt_is_printed(
                new Receipt("LOCATION: 100 Main Street, NewYork 12345,USA"),
                new Receipt("CARD NO: 123456"),
                new Receipt("DATE 01/07/2015 TIME 17:05:00"),
                new Receipt("TERMINAL 47"),
                new Receipt("TRANSACTION#00000000-0000-0001-0000-000000000002"),
                new Receipt("DEPOSIT OF $20.00")
        );
    }

    @Test
    @Withdraw
    public void withdraw_cash_from_account() {
        given().the_account_balance_is_$_dollars("300");
        $.and().the_valid_pin_number_has_been_entered();

        when().a_withdraw_of_$_dollars_is_made("20.00");

        then().the_ATM_dispenses_$_dollars("20.00")
                .and().the_account_balance_is_$_dollars("280.00")
                .and().the_card_is_returned()
                .and().a_receipt_is_printed(
                new Receipt("LOCATION: 100 Main Street, NewYork 12345,USA"),
                new Receipt("CARD NO: 123456"),
                new Receipt("DATE 01/07/2015 TIME 17:05:00"),
                new Receipt("TERMINAL 47"),
                new Receipt("TRANSACTION#00000000-0000-0001-0000-000000000002"),
                new Receipt("WITHDRAW OF $20.00")
        );
    }

    @Test
    public void balance_inquiry(){
        given().the_account_balance_is_$_dollars("300.00");
                $.and().the_valid_pin_number_has_been_entered();

        when().a_balance_inquiry_is_made();

        then().the_card_is_returned()
                .and().a_receipt_is_printed(
                new Receipt("LOCATION: 100 Main Street, NewYork 12345,USA"),
                new Receipt("CARD NO: 123456"),
                new Receipt("DATE 01/07/2015 TIME 17:05:00"),
                new Receipt("TERMINAL 47"),
                new Receipt("BALANCE $300.00")
        );
    }
}

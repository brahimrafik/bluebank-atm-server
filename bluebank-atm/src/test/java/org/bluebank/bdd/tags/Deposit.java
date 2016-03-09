package org.bluebank.bdd.tags;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Transaction
@IsTag(value = "Deposit",
        description =  "Enables account holder to handleDepositResponse funds into an account held at a bank" ,
        color = "#9BD55D")
@Retention( RetentionPolicy.RUNTIME )
public @interface Deposit {
}

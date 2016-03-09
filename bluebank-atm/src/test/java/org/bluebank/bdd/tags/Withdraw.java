package org.bluebank.bdd.tags;

import com.tngtech.jgiven.annotation.IsTag;

@Transaction
@IsTag(value = "Withdraw",
        description = "Enables account holder to withdraw funds from an account held at a bank",
        color = "#CE71D9")
public @interface Withdraw {
}

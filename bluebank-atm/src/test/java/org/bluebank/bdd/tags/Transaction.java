package org.bluebank.bdd.tags;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag(type = "Feature", value = "Transaction", color = "#D87296")
@Retention( RetentionPolicy.RUNTIME )
public @interface Transaction {
}

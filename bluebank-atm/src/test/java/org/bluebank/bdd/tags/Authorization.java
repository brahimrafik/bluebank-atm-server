package org.bluebank.bdd.tags;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag( type = "Feature",
        value = "Authorization",
        description = "The authorization starts after a customer has entered his card in the ATM" ,
        color = "#9D99E5")
@Retention( RetentionPolicy.RUNTIME )
public @interface Authorization {
}

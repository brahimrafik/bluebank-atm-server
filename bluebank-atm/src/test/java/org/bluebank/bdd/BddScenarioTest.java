package org.bluebank.bdd;


import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;

public abstract class BddScenarioTest<GIVEN, WHEN, THEN> extends ScenarioTest<GIVEN, WHEN, THEN> {

    @ProvidedScenarioState
    private BddComponent bddComponent;

    public BddScenarioTest() {
        bddComponent = DaggerBddComponent.create();
    }
}

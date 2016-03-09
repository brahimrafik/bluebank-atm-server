package org.bluebank.atm;

import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.bluebank.AtmModule.ATMConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AtmHealthCheckTest {

    private HealthCheckRegistry healthCheckRegistry;
    private ATMConfiguration atmConfiguration;

    @Before
    public void before() {
        atmConfiguration = mock(ATMConfiguration.class);
        healthCheckRegistry = new HealthCheckRegistry();
    }

    @Test
    public void cash_dispenser_should_be_healthy() {
        // given
        when(atmConfiguration.getCashOnHand()).thenReturn(new BigDecimal("10"));
        Atm atm = new Atm(atmConfiguration, healthCheckRegistry);

        //when
        Result result = healthCheckRegistry.runHealthCheck("ATM Service");

        //then
        assertThat(result.isHealthy()).isTrue();
        assertThat(result.getMessage()).isEqualTo("Cash on hand is $ 10");
    }

    @Test
    public void cash_dispenser_should_be_unhealthy() {
        // given
        when(atmConfiguration.getCashOnHand()).thenReturn(ZERO);
        Atm atm = new Atm(atmConfiguration, healthCheckRegistry);

        //when
        Result result = healthCheckRegistry.runHealthCheck("ATM Service");

        //then
        assertThat(result.isHealthy()).isFalse();
        assertThat(result.getMessage()).isEqualTo("There is no cash available");
    }
}

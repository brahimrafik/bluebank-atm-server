package org.bluebank.atm;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.bluebank.AtmModule.ATMConfiguration;
import org.bluebank.atm.authorization.model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;

import static com.codahale.metrics.health.HealthCheck.Result.healthy;
import static com.codahale.metrics.health.HealthCheck.Result.unhealthy;
import static java.math.BigDecimal.ZERO;

@Singleton
public class Atm {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private BigDecimal cashOnHand;

    @Inject
    public Atm(ATMConfiguration atmConfiguration, HealthCheckRegistry healthCheckRegistry) {
        this.cashOnHand = atmConfiguration.getCashOnHand();
        healthCheckRegistry.register("ATM Service", new AtmHealthCheck());
    }

    public Card readCard(String cardNumber) {
        logger.info("Reading card id {}", cardNumber);
        return new Card(cardNumber);
    }

    public void ejectCard() {
        logger.info("Card has been ejected");
    }

    public void retainCard() {
        logger.info("Card has been retained");
    }

    public void addCash(BigDecimal cash) {
        cashOnHand = cashOnHand.add(cash);
    }

    public boolean checkCashOnHand(BigDecimal amount) {
        return amount.compareTo(cashOnHand) < 1;
    }

    public void dispenseCash(BigDecimal amount) {
        cashOnHand = cashOnHand.subtract(amount);
    }


    private class AtmHealthCheck extends HealthCheck {
        @Override
        protected Result check() throws Exception {
            Result result;
            if (cashOnHand.compareTo(ZERO) > 0) {
                result = healthy("Cash on hand is $ %s", cashOnHand);
            } else {
                result = unhealthy("There is no cash available");
            }
            return result;
        }
    }
}

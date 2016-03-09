package org.bluebank.atm.authorization.outbound;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.bluebank.banking.Bank;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.bluebank.atm.Transaction.PinValidationRequested;

@Singleton
public class PinValidationRequestHandler {
    private final Bank bank;

    @Inject
    public PinValidationRequestHandler(EventBus eventBus, Bank bank) {
        this.bank = bank;
        eventBus.register(this);
    }

    @Subscribe
    public void requestPinValidation(PinValidationRequested pinValidationRequested) {
        bank.performPinValidation(
                pinValidationRequested.id,
                pinValidationRequested.accountNumber,
                pinValidationRequested.pin
        );
    }
}

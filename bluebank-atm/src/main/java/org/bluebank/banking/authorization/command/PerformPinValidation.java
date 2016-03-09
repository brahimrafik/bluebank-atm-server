package org.bluebank.banking.authorization.command;


import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.banking.account.model.Account;

import java.util.UUID;

public class PerformPinValidation extends AggregateRootCommand<Account> {
    private final UUID atmTransaction;
    private final String pin;

    public PerformPinValidation(UUID transactionId,
                                Repository<Account> repository,
                                DomainEventsDispatcher eventsDispatcher,
                                UUID atmTransaction,
                                String pin) {
        super(transactionId, repository, eventsDispatcher);
        this.atmTransaction = atmTransaction;
        this.pin = pin;
    }

    @Override
    public void delegateToAggregateRoot(Account account) {
        account.checkPin(atmTransaction, pin);
    }
}

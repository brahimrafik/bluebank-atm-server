package org.bluebank.atm.authorization.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.atm.Transaction;

import java.util.UUID;

public class RequestPinValidation extends AggregateRootCommand<Transaction> {
    private final String pin;

    public RequestPinValidation(UUID sessionId,
                                Repository<Transaction> repository,
                                DomainEventsDispatcher domainEventsDispatcher,
                                String pin) {
        super(sessionId, repository, domainEventsDispatcher);
        this.pin = pin;
    }

    @Override
    public void delegateToAggregateRoot(Transaction transaction) {
        transaction.requestPinValidation(pin);
    }
}

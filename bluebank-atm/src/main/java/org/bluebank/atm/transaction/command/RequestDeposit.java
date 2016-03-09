package org.bluebank.atm.transaction.command;


import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class RequestDeposit extends AggregateRootCommand<Transaction> {
    private final BigDecimal amount;

    public RequestDeposit(UUID sessionId,
                          Repository<Transaction> repository,
                          DomainEventsDispatcher eventsDispatcher,
                          BigDecimal amount) {
        super(sessionId, repository, eventsDispatcher);
        this.amount = amount;
    }

    @Override
    public void delegateToAggregateRoot(Transaction transaction) {
        transaction.requestDeposit(amount);
    }
}

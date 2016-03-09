package org.bluebank.atm.transaction.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class RequestWithdraw extends AggregateRootCommand<Transaction> {
    private final BigDecimal amount;

    public RequestWithdraw(UUID aggregateRootId,
                           Repository<Transaction> repository,
                           DomainEventsDispatcher domainEventsDispatcher,
                           BigDecimal amount) {
        super(aggregateRootId, repository, domainEventsDispatcher);
        this.amount = amount;
    }

    @Override
    public void delegateToAggregateRoot(Transaction transaction) {
        transaction.requestWithdraw(amount);
    }
}

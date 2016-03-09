package org.bluebank.atm.transaction.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;

import java.util.UUID;

public class RequestInquiry extends AggregateRootCommand<Transaction> {

    public RequestInquiry(UUID sessionId,
                          Repository<Transaction> repository,
                          DomainEventsDispatcher eventsDispatcher) {
        super(sessionId, repository, eventsDispatcher);
    }

    @Override
    public void delegateToAggregateRoot(Transaction transaction) {
        transaction.requestBalance();
    }
}

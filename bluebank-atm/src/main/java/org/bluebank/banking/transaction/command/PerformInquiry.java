package org.bluebank.banking.transaction.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.banking.account.model.Account;

import java.util.UUID;

public class PerformInquiry extends AggregateRootCommand<Account> {
    private final UUID atmTransaction;

    public PerformInquiry(UUID transactionId,
                          Repository<Account> repository,
                          DomainEventsDispatcher eventsDispatcher,
                          UUID atmTransaction) {
        super(transactionId, repository, eventsDispatcher);
        this.atmTransaction = atmTransaction;
    }

    @Override
    public void delegateToAggregateRoot(Account account) {
        account.performInquiry(atmTransaction);
    }
}

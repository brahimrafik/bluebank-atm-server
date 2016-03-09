package org.bluebank.banking.transaction.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.banking.account.model.Account;

import java.math.BigDecimal;
import java.util.UUID;

public class PerformDeposit extends AggregateRootCommand<Account> {
    private final UUID atmTransaction;
    private final BigDecimal amount;

    public PerformDeposit(UUID transactionId,
                          Repository<Account> repository,
                          DomainEventsDispatcher eventsDispatcher,
                          UUID atmTransaction,
                          BigDecimal amount) {
        super(transactionId, repository, eventsDispatcher);
        this.atmTransaction = atmTransaction;
        this.amount = amount;
    }

    @Override
    public void delegateToAggregateRoot(Account account) {
        account.performDeposit(atmTransaction, amount);
    }
}

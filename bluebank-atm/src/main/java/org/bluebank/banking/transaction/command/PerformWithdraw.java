package org.bluebank.banking.transaction.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.banking.account.model.Account;

import java.math.BigDecimal;
import java.util.UUID;

public class PerformWithdraw extends AggregateRootCommand<Account> {
    private final UUID atmTransaction;
    private final BigDecimal amount;

    public PerformWithdraw(UUID transactionId,
                              Repository<Account> repository,
                              DomainEventsDispatcher domainEventsDispatcher,
                              UUID atmTransaction,
                              BigDecimal amount) {
        super(transactionId, repository, domainEventsDispatcher);
        this.atmTransaction = atmTransaction;
        this.amount = amount;
    }

    @Override
    public void delegateToAggregateRoot(Account account) {
        account.performWithdraw(atmTransaction, amount);
    }
}

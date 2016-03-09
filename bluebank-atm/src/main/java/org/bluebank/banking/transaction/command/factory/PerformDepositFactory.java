package org.bluebank.banking.transaction.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.banking.account.model.Account;
import org.bluebank.banking.transaction.command.PerformDeposit;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.UUID;

@Singleton
public class PerformDepositFactory {
    private final Repository<Account> repository;
    private final DomainEventsDispatcher eventsDispatcher;

    @Inject
    public PerformDepositFactory(Repository<Account> repository,
                                 DomainEventsDispatcher eventsDispatcher) {
        this.repository = repository;
        this.eventsDispatcher = eventsDispatcher;
    }

    public PerformDeposit build(UUID atmTransaction, UUID id, BigDecimal amount) {
        return new PerformDeposit(id, repository, eventsDispatcher, atmTransaction, amount);
    }
}

package org.bluebank.atm.transaction.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.transaction.command.RequestDeposit;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.UUID;

@Singleton
public class RequestDepositFactory {
    private final Repository<Transaction> repository;
    private final DomainEventsDispatcher eventsDispatcher;

    @Inject
    public RequestDepositFactory(Repository<Transaction> repository,
                                 DomainEventsDispatcher eventsDispatcher) {
        this.repository = repository;
        this.eventsDispatcher = eventsDispatcher;
    }

    public RequestDeposit build(UUID sessionId, BigDecimal amount) {
        return new RequestDeposit(
                sessionId,
                repository,
                eventsDispatcher,
                amount
        );
    }
}

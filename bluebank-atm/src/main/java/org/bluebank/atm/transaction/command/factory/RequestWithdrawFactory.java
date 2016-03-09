package org.bluebank.atm.transaction.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.transaction.command.RequestWithdraw;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.UUID;

@Singleton
public class RequestWithdrawFactory {
    private final Repository<Transaction> repository;
    private final DomainEventsDispatcher eventsDispatcher;

    @Inject
    public RequestWithdrawFactory(Repository<Transaction> repository,
                                  DomainEventsDispatcher eventsDispatcher) {
        this.repository = repository;
        this.eventsDispatcher = eventsDispatcher;
    }

    public RequestWithdraw build(UUID sessionId, BigDecimal amount) {
        return new RequestWithdraw(
                sessionId,
                repository,
                eventsDispatcher,
                amount);
    }
}

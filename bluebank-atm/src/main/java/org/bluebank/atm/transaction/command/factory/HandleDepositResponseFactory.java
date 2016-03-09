package org.bluebank.atm.transaction.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.transaction.command.HandleDepositResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.UUID;

@Singleton
public class HandleDepositResponseFactory {
    private final Repository<Transaction> repository;
    private final DomainEventsDispatcher eventsDispatcher;

    @Inject
    public HandleDepositResponseFactory(Repository<Transaction> repository,
                                        DomainEventsDispatcher eventsDispatcher) {
        this.repository = repository;
        this.eventsDispatcher = eventsDispatcher;
    }

    public HandleDepositResponse build(UUID sessionId, BigDecimal amount, String cardNumber, UUID transactionID) {
        return new HandleDepositResponse(
                sessionId,
                repository,
                eventsDispatcher,
                transactionID,
                amount,
                cardNumber
        );
    }
}

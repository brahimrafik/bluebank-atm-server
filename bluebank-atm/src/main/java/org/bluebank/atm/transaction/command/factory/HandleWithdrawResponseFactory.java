package org.bluebank.atm.transaction.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.transaction.command.HandleWithdrawResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.UUID;

@Singleton
public class HandleWithdrawResponseFactory {
    private final Repository<Transaction> repository;
    private final DomainEventsDispatcher eventsDispatcher;

    @Inject
    public HandleWithdrawResponseFactory(Repository<Transaction> repository,
                                         DomainEventsDispatcher eventsDispatcher) {
        this.repository = repository;
        this.eventsDispatcher = eventsDispatcher;
    }

    public HandleWithdrawResponse build(UUID sessionId, BigDecimal amount, String cardNumber, UUID transactionID) {
        return new HandleWithdrawResponse(
                sessionId,
                repository,
                eventsDispatcher,
                transactionID,
                amount,
                cardNumber
        );
    }
}

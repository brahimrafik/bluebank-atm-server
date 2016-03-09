package org.bluebank.atm.transaction.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.transaction.command.HandleInquiryResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.UUID;

@Singleton
public class HandleInquiryResponseFactory {
    private final Repository<Transaction> repository;
    private final DomainEventsDispatcher eventsDispatcher;

    @Inject
    public HandleInquiryResponseFactory(Repository<Transaction> repository,
                                        DomainEventsDispatcher eventsDispatcher) {
        this.repository = repository;
        this.eventsDispatcher = eventsDispatcher;
    }

    public HandleInquiryResponse build(UUID sessionId, BigDecimal balance, String cardNumber) {
        return new HandleInquiryResponse(
                sessionId,
                repository,
                eventsDispatcher,
                balance,
                cardNumber
        );
    }
}

package org.bluebank.atm.authorization.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.authorization.command.HandleCardValidationResponse;
import org.bluebank.atm.authorization.model.Card;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class HandleCardValidationResponseFactory {
    private final Repository<Transaction> repository;
    private final DomainEventsDispatcher domainEventsDispatcher;

    @Inject
    public HandleCardValidationResponseFactory(Repository<Transaction> repository,
                                               DomainEventsDispatcher domainEventsDispatcher) {
        this.repository = repository;
        this.domainEventsDispatcher = domainEventsDispatcher;
    }

    public HandleCardValidationResponse build(UUID id, Card card) {
        return new HandleCardValidationResponse(id, repository, domainEventsDispatcher, card);
    }
}

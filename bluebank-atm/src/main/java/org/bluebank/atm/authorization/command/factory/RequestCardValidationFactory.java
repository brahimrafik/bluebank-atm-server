package org.bluebank.atm.authorization.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.authorization.command.RequestCardValidation;
import org.bluebank.atm.authorization.model.AtmInfo;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class RequestCardValidationFactory {
    private final Repository<Transaction> repository;
    private final DomainEventsDispatcher domainEventsDispatcher;

    @Inject
    public RequestCardValidationFactory(Repository<Transaction> repository,
                                        DomainEventsDispatcher domainEventsDispatcher) {
        this.repository = repository;
        this.domainEventsDispatcher = domainEventsDispatcher;
    }

    public RequestCardValidation build(UUID id, String cardNumber, AtmInfo atmInfo) {
        return new RequestCardValidation(id, repository, domainEventsDispatcher, cardNumber, atmInfo);
    }
}

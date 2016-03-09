package org.bluebank.atm.authorization.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.authorization.command.RequestPinValidation;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class RequestPinValidationFactory {
    private final Repository<Transaction> repository;
    private final DomainEventsDispatcher domainEventsDispatcher;

    @Inject
    public RequestPinValidationFactory(Repository<Transaction> repository,
                                       DomainEventsDispatcher domainEventsDispatcher) {
        this.repository = repository;
        this.domainEventsDispatcher = domainEventsDispatcher;
    }

    public RequestPinValidation build(UUID id, String pin) {
        return new RequestPinValidation(id, repository, domainEventsDispatcher, pin);
    }
}

package org.bluebank.atm.authorization.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.authorization.command.HandlePinValidationResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

import static org.bluebank.banking.authorization.outbound.PinValidationResponse.ValidationStatus;

@Singleton
public class HandlePinValidationResponseFactory {
    private final Repository<Transaction> repository;
    private final DomainEventsDispatcher domainEventsDispatcher;

    @Inject
    public HandlePinValidationResponseFactory(Repository<Transaction> repository,
                                              DomainEventsDispatcher domainEventsDispatcher) {
        this.repository = repository;
        this.domainEventsDispatcher = domainEventsDispatcher;
    }

    public HandlePinValidationResponse build(UUID id, ValidationStatus validationStatus) {
        return new HandlePinValidationResponse(
                id,
                repository,
                domainEventsDispatcher,
                validationStatus
        );
    }
}

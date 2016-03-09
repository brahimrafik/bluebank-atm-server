package org.bluebank.banking.authorization.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.banking.account.model.Account;
import org.bluebank.banking.authorization.command.PerformPinValidation;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class PerformPinValidationFactory {
    private final Repository<Account> repository;
    private final DomainEventsDispatcher eventsDispatcher;

    @Inject
    public PerformPinValidationFactory(Repository<Account> repository,
                                       DomainEventsDispatcher eventsDispatcher) {
        this.repository = repository;
        this.eventsDispatcher = eventsDispatcher;
    }

    public PerformPinValidation build(UUID atmTransaction, UUID id, String pin) {
        return new PerformPinValidation(
                id,
                repository,
                eventsDispatcher,
                atmTransaction,
                pin
        );
    }
}

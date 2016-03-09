package org.bluebank.banking.account.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.banking.account.command.CreateAccount;
import org.bluebank.banking.account.model.Account;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class CreateAccountFactory {
    private final Repository<Account> repository;
    private final DomainEventsDispatcher eventsDispatcher;

    @Inject
    public CreateAccountFactory(Repository<Account> repository,
                                DomainEventsDispatcher domainEventsDispatcher) {
        this.repository = repository;
        this.eventsDispatcher = domainEventsDispatcher;
    }

    public CreateAccount build(UUID id, String cardNumber, String pin) {
        return new CreateAccount(id, repository, eventsDispatcher, cardNumber, pin);
    }
}

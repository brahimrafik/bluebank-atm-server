package org.bluebank.banking.account.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.command.FirstAggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.banking.account.model.Account;

import java.util.UUID;

public class CreateAccount extends FirstAggregateRootCommand<Account> {
    private final String cardNumber;
    private final String pin;

    public CreateAccount(UUID transactionId,
                         Repository<Account> repository,
                         DomainEventsDispatcher eventsDispatcher,
                         String cardNumber,
                         String pin) {
        super(transactionId, repository, eventsDispatcher);
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    @Override
    public void delegateToAggregateRoot(Account aggregateRoot) {
        aggregateRoot.createAccount(cardNumber, pin);
    }
}

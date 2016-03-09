package org.bluebank.atm.authorization.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.command.FirstAggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.authorization.model.AtmInfo;

import java.util.UUID;

public class RequestCardValidation extends FirstAggregateRootCommand<Transaction> {
    private final String cardNumber;
    private final AtmInfo atmInfo;

    public RequestCardValidation(UUID sessionId,
                                 Repository<Transaction> repository,
                                 DomainEventsDispatcher domainEventsDispatcher,
                                 String cardNumber,
                                 AtmInfo atmInfo) {
        super(sessionId, repository, domainEventsDispatcher);
        this.cardNumber = cardNumber;
        this.atmInfo = atmInfo;
    }

    @Override
    public void delegateToAggregateRoot(Transaction aggregateRoot) {
        aggregateRoot.requestCardValidation(cardNumber, atmInfo);
    }

}

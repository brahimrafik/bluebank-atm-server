package org.bluebank.atm.authorization.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.authorization.model.Card;

import java.util.UUID;

public class HandleCardValidationResponse extends AggregateRootCommand<Transaction> {
    private final Card card;

    public HandleCardValidationResponse(UUID sessionId,
                                        Repository<Transaction> repository,
                                        DomainEventsDispatcher domainEventsDispatcher,
                                        Card card) {
        super(sessionId, repository, domainEventsDispatcher);
        this.card = card;
    }

    @Override
    public void delegateToAggregateRoot(Transaction transaction) {
        transaction.handleCardValidation(card);
    }
}

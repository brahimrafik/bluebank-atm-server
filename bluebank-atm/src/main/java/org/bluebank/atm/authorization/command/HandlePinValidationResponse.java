package org.bluebank.atm.authorization.command;

import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.atm.Transaction;

import java.util.UUID;

import static org.bluebank.banking.authorization.outbound.PinValidationResponse.ValidationStatus;

public class HandlePinValidationResponse extends AggregateRootCommand<Transaction> {
    private final ValidationStatus validationStatus;

    public HandlePinValidationResponse(UUID sessionId,
                                       Repository<Transaction> repository,
                                       DomainEventsDispatcher domainEventsDispatcher,
                                       ValidationStatus validationStatus) {
        super(sessionId, repository, domainEventsDispatcher);
        this.validationStatus = validationStatus;
    }

    @Override
    public void delegateToAggregateRoot(Transaction transaction) {
        transaction.handlePinValidation(validationStatus);
    }
}

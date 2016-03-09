package org.bluebank.atm.transaction.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.atm.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class HandleInquiryResponse extends AggregateRootCommand<Transaction> {
    private final BigDecimal balance;
    private final String cardNumber;

    public HandleInquiryResponse(UUID sessionId,
                                 Repository<Transaction> repository,
                                 DomainEventsDispatcher eventsDispatcher,
                                 BigDecimal balance,
                                 String cardNumber) {
        super(sessionId, repository, eventsDispatcher);
        this.balance = balance;
        this.cardNumber = cardNumber;
    }

    @Override
    public void delegateToAggregateRoot(Transaction transaction) {
        transaction.handleInquiryResponse(balance, cardNumber);
    }
}

package org.bluebank.atm.transaction.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class HandleWithdrawResponse extends AggregateRootCommand<Transaction> {
    private final UUID transactionId;
    private final BigDecimal amount;
    private final String cardNumber;

    public HandleWithdrawResponse(UUID sessionId,
                                  Repository<Transaction> repository,
                                  DomainEventsDispatcher eventsDispatcher,
                                  UUID transactionId,
                                  BigDecimal amount,
                                  String cardNumber) {
        super(sessionId, repository, eventsDispatcher);
        this.transactionId = transactionId;
        this.amount = amount;
        this.cardNumber = cardNumber;
    }

    @Override
    public void delegateToAggregateRoot(Transaction transaction) {
        transaction.handleWithdrawResponse(transactionId, amount, cardNumber);
    }
}

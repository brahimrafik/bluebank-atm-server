package org.bluebank.banking.transaction.model;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionConfirmation {
    public final UUID id;
    public final BigDecimal amount;
    public final String cardNumber;
    public final UUID transactionId;

    public TransactionConfirmation(UUID id, BigDecimal amount, String cardNumber, UUID transactionId) {
        this.id = id;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.transactionId = transactionId;
    }
}

package org.bluebank.banking.transaction.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Transaction {
    public final Date date;
    public final UUID id;
    public final BigDecimal amount;
    public final TransactionType type;

    public Transaction(UUID id, BigDecimal amount, TransactionType type) {
        this.id = id;
        date = new Date();
        this.amount = amount;
        this.type = type;
    }

    public static Transaction newDeposit(UUID id, BigDecimal amount) {
        return new Transaction(id, amount, TransactionType.DEPOSIT);
    }

    public static Transaction newWithdrawal(UUID id, BigDecimal amount) {
        return new Transaction(id, amount.negate(), TransactionType.WITHDRAWAL);
    }

    private enum TransactionType {
        DEPOSIT, WITHDRAWAL
    }
}

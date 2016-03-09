package org.bluebank.banking.transaction.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Inquiry {
    public final UUID id;
    public final BigDecimal balance;
    public final String cardNumber;

    public Inquiry(UUID id, BigDecimal balance, String cardNumber) {
        this.id = id;
        this.balance = balance;
        this.cardNumber = cardNumber;
    }
}

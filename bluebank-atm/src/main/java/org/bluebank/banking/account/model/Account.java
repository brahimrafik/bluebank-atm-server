package org.bluebank.banking.account.model;

import org.bluebank.api.domain.AggregateRoot;
import org.bluebank.api.domain.DomainEvent;
import org.bluebank.api.domain.IdGenerator;
import org.bluebank.banking.transaction.model.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.math.BigDecimal.ROUND_DOWN;
import static java.math.BigDecimal.ZERO;
import static org.bluebank.banking.transaction.model.Transaction.newDeposit;
import static org.bluebank.banking.transaction.model.Transaction.newWithdrawal;

public class Account extends AggregateRoot {
    private final UUID id;
    private final IdGenerator idGenerator;
    private String cardNumber;
    private Pin pin;
    private List<Transaction> transactions;

    public Account(UUID id, IdGenerator idGenerator) {
        this.id = id;
        this.idGenerator = idGenerator;
        transactions = new ArrayList<>();
    }

    public void createAccount(String cardNumber, String pin) {
        applyEvent(new accountCreated(getId(), cardNumber, pin));
    }

    private static class accountCreated extends DomainEvent<Account> {

        private final String cardNumber;
        public final String pin;

        public accountCreated(UUID id, String cardNumber, String pin) {
            super(id);
            this.cardNumber = cardNumber;
            this.pin = pin;
        }

        @Override
        public void apply(Account account) {
            account.cardNumber = cardNumber;
            account.pin = new Pin(pin);
        }
    }

    public void performInquiry(UUID atmTransaction) {
        applyEvent(new InquiryPerformed(getId(), atmTransaction, getBalance(), cardNumber));
    }

    public static class InquiryPerformed extends DomainEvent<Account> {

        public final UUID atmTransaction;
        public final BigDecimal balance;
        public final String cardNumber;

        public InquiryPerformed(UUID id, UUID atmTransaction, BigDecimal balance, String cardNumber) {
            super(id);
            this.atmTransaction = atmTransaction;
            this.balance = balance;
            this.cardNumber = cardNumber;
        }

        @Override
        public void apply(Account account) {
        }
    }


    public void performDeposit(UUID atmTransaction, BigDecimal amount) {
        applyEvent(new DepositPerformed(getId(), atmTransaction, amount, cardNumber, idGenerator.generate()));
    }


    public static class DepositPerformed extends DomainEvent<Account> {

        public final UUID atmTransaction;
        public final BigDecimal amount;
        public final String cardNumber;
        public final UUID transactionId;

        public DepositPerformed(UUID id,
                                UUID atmTransaction,
                                BigDecimal amount,
                                String cardNumber,
                                UUID transactionId) {
            super(id);
            this.atmTransaction = atmTransaction;
            this.amount = amount;
            this.cardNumber = cardNumber;
            this.transactionId = transactionId;
        }

        @Override
        public void apply(Account account) {
            Transaction transaction = newDeposit(transactionId, amount);
            account.transactions.add(transaction);
        }
    }

    public void performWithdraw(UUID atmTransaction, BigDecimal amount) {
        applyEvent(new WithdrawPerformed(getId(), atmTransaction, amount, cardNumber, idGenerator.generate()));
    }

    public static class WithdrawPerformed extends DomainEvent<Account> {

        public final UUID atmTransaction;
        public final BigDecimal amount;
        public final String cardNumber;
        public final UUID transactionId;

        public WithdrawPerformed(UUID id,
                                UUID atmTransaction,
                                BigDecimal amount,
                                String cardNumber,
                                UUID transactionId) {
            super(id);
            this.atmTransaction = atmTransaction;
            this.amount = amount;
            this.cardNumber = cardNumber;
            this.transactionId = transactionId;
        }

        @Override
        public void apply(Account account) {
            Transaction transaction = newWithdrawal(transactionId, amount);
            account.transactions.add(transaction);
        }
    }

    public BigDecimal getBalance() {
        return transactions.stream()
                .map(transaction -> transaction.amount)
                .reduce(ZERO, BigDecimal::add)
                .setScale(2, ROUND_DOWN);
    }

    public void checkPin(UUID atmTransaction, String providedPin) {
        if (pin.check(providedPin)) {
            applyEvent(new PinValidated(getId(), atmTransaction));
        } else if (pin.getTriesRemaining() == 0) {
            applyEvent(new CardDisabled(getId(), atmTransaction));
        } else {
            applyEvent(new PinValidationFailed(getId(), atmTransaction));
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public static class PinValidated extends DomainEvent<Account> {

        public final UUID atmTransaction;

        public PinValidated(UUID id, UUID atmTransaction) {
            super(id);
            this.atmTransaction = atmTransaction;
        }

        @Override
        public void apply(Account account) {

        }
    }

    public static class CardDisabled extends DomainEvent<Account> {

        public final UUID atmTransaction;

        public CardDisabled(UUID id, UUID atmTransaction) {
            super(id);
            this.atmTransaction = atmTransaction;
        }

        @Override
        public void apply(Account account) {

        }
    }

    public static class PinValidationFailed extends DomainEvent<Account> {

        public final UUID atmTransaction;

        public PinValidationFailed(UUID id, UUID atmTransaction) {
            super(id);
            this.atmTransaction = atmTransaction;
        }

        @Override
        public void apply(Account account) {
        }
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    private static class Pin {
        private final String value;
        private static final int TRY_LIMIT = 3;
        private int triesLeft = TRY_LIMIT;

        private Pin(String value) {
            this.value = value;
        }

        public boolean check(final String providedPin) {

            if (triesLeft == 0) {
                return false;
            }

            decrementTriesRemaining();

            boolean result;
            if (value.equals(providedPin)) {
                resetTriesRemaining();
                result = true;
            } else {
                result = false;
            }
            return result;
        }

        private void decrementTriesRemaining() {
            triesLeft--;
        }

        public int getTriesRemaining() {
            return triesLeft;
        }

        private void resetTriesRemaining() {
            triesLeft = TRY_LIMIT;
        }
    }

    @Override
    public String toString() {
        return toStringHelper()
                .add("cardNumber", cardNumber)
                .add("transactions", transactions)
                .toString();
    }
}

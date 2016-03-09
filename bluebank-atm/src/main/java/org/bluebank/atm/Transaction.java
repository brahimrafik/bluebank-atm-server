package org.bluebank.atm;

import org.bluebank.api.domain.AggregateRoot;
import org.bluebank.api.domain.DomainEvent;
import org.bluebank.atm.authorization.model.AtmInfo;
import org.bluebank.atm.authorization.model.Card;
import org.bluebank.banking.authorization.outbound.PinValidationResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

public class Transaction extends AggregateRoot {
    private final UUID id;
    private final List<States> states;
    private Card card;
    private AtmInfo atmInfo;

    public Transaction(UUID id) {
        this.id = id;
        states = newArrayList(States.INITIAL);
    }

    public void requestCardValidation(String accountNumber, AtmInfo atmInfo) {
        applyEvent(new CardValidationRequested(id, accountNumber, atmInfo));
    }

    public void handleCardValidation(Card card) {
        applyEvent(new CardValidated(id, card));
    }

    public void requestPinValidation(String providedPin) {
        applyEvent(new PinValidationRequested(id, card.cardNumber, providedPin));
    }

    public void handlePinValidation(PinValidationResponse.ValidationStatus validationStatus) {
        switch (validationStatus) {
            case VALID:
                applyEvent(new PinValidated(id));
                break;
            case WRONG_PIN:
                applyEvent(new PinInvalid(id));
                break;
            case DISABLED:
                applyEvent(new CardRetained(id));
                applyEvent(new Closed(id));
                break;
            default:
                throw new IllegalArgumentException(format("Validation status of type %s not supported", validationStatus));
        }
    }

    public void handleInquiryResponse(BigDecimal balance, String cardNumber) {
        applyEvent(new Inquired(id, balance, cardNumber, atmInfo));
    }

    public void requestDeposit(BigDecimal amount) {
        applyEvent(new DepositRequested(id, amount, card.cardNumber));
    }

    public void requestWithdraw(BigDecimal amount) {
        applyEvent(new WithdrawRequested(id, amount, card.cardNumber));
    }

    public void handleDepositResponse(UUID transactionId, BigDecimal amount, String cardNumber) {
        applyEvent(new DepositPerformed(id, amount, cardNumber, transactionId, atmInfo));
        applyEvent(new Closed(id));
    }

    public void handleWithdrawResponse(UUID transactionId, BigDecimal amount, String cardNumber) {
        applyEvent(new WithdrawPerformed(id, amount, cardNumber, transactionId, atmInfo));
        applyEvent(new Closed(id));
    }

    public void requestBalance() {
        applyEvent(new InquiryRequested(id, card.cardNumber));
        applyEvent(new Closed(id));
    }

    @Override
    public UUID getId() {
        return id;
    }

    public static class Inquired extends DomainEvent<Transaction> {
        public final BigDecimal balance;
        public final String accountNumber;
        public final AtmInfo atmInfo;

        public Inquired(UUID id,
                        BigDecimal balance,
                        String accountNumber, AtmInfo atmInfo) {
            super(id);
            this.balance = balance;
            this.accountNumber = accountNumber;
            this.atmInfo = atmInfo;
        }

        @Override
        public void apply(Transaction transaction) {

        }
    }

    public static class CardValidationRequested extends DomainEvent<Transaction> {
        public final UUID id;
        public final String accountNumber;
        public final AtmInfo atmInfo;

        public CardValidationRequested(UUID id, String accountNumber, AtmInfo atmInfo) {
            super(id);
            this.id = id;
            this.accountNumber = accountNumber;
            this.atmInfo = atmInfo;
        }

        @Override
        public void apply(Transaction transaction) {
            transaction.atmInfo = atmInfo;
            transaction.states.add(States.CARD_READING);
        }
    }

    public static class CardValidated extends DomainEvent<Transaction> {
        private final Card card;

        public CardValidated(UUID id, Card card) {
            super(id);
            this.card = card;
        }

        @Override
        public void apply(Transaction transaction) {
            transaction.states.add(States.PIN_VALIDATING);
            transaction.card = card;
        }
    }

    public static class PinValidationRequested extends DomainEvent<Transaction> {
        public final String accountNumber;
        public final String pin;

        public PinValidationRequested(UUID id, String accountNumber, String pin) {
            super(id);
            this.accountNumber = accountNumber;
            this.pin = pin;
        }

        @Override
        public void apply(Transaction transaction) {
        }
    }

    public static class PinValidated extends DomainEvent<Transaction> {

        public PinValidated(UUID id) {
            super(id);
        }

        @Override
        public void apply(Transaction transaction) {
            transaction.states.add(States.DISPENSING);
        }
    }

    public static class PinInvalid extends DomainEvent<Transaction> {

        public PinInvalid(UUID id) {
            super(id);
        }

        @Override
        public void apply(Transaction transaction) {
            transaction.states.add(States.PIN_VALIDATING);
        }
    }

    public static class CardRetained extends DomainEvent<Transaction> {

        public CardRetained(UUID id) {
            super(id);
        }

        @Override
        public void apply(Transaction transaction) {
            transaction.states.add(States.CARD_RETAINING);
        }
    }

    public static class DepositRequested extends DomainEvent<Transaction> {
        public final BigDecimal amount;
        public final String cardNumber;

        public DepositRequested(UUID id, BigDecimal amount, String cardNumber) {
            super(id);
            this.amount = amount;
            this.cardNumber = cardNumber;
        }

        @Override
        public void apply(Transaction transaction) {
            transaction.states.add(States.DEPOSITING);
        }
    }

    public static class WithdrawRequested extends DomainEvent<Transaction> {
        public final BigDecimal amount;
        public final String cardNumber;

        public WithdrawRequested(UUID id, BigDecimal amount, String cardNumber) {
            super(id);
            this.amount = amount;
            this.cardNumber = cardNumber;
        }

        @Override
        public void apply(Transaction transaction) {
            transaction.states.add(States.WITHDRAWING);
        }
    }

    public class DepositPerformed extends DomainEvent<Transaction> {
        public final BigDecimal amount;
        public final String cardNumber;
        public final UUID transactionId;
        public final AtmInfo atmInfo;

        public DepositPerformed(UUID id,
                                BigDecimal amount,
                                String cardNumber,
                                UUID transactionId,
                                AtmInfo atmInfo) {
            super(id);
            this.amount = amount;
            this.cardNumber = cardNumber;
            this.transactionId = transactionId;
            this.atmInfo = atmInfo;
        }

        @Override
        public void apply(Transaction transaction) {

        }
    }

    public class WithdrawPerformed extends DomainEvent<Transaction> {
        public final BigDecimal amount;
        public final String cardNumber;
        public final UUID transactionId;
        public final AtmInfo atmInfo;

        public WithdrawPerformed(UUID id,
                                BigDecimal amount,
                                String cardNumber,
                                UUID transactionId,
                                AtmInfo atmInfo) {
            super(id);
            this.amount = amount;
            this.cardNumber = cardNumber;
            this.transactionId = transactionId;
            this.atmInfo = atmInfo;
        }

        @Override
        public void apply(Transaction transaction) {

        }
    }

    public static class InquiryRequested extends DomainEvent<Transaction> {
        public final String accountNumber;

        public InquiryRequested(UUID id, String accountNumber) {
            super(id);
            this.accountNumber = accountNumber;
        }

        @Override
        public void apply(Transaction transaction) {
            transaction.states.add(States.INQUIRING);
        }
    }

    public static class Closed extends DomainEvent<Transaction> {

        public Closed(UUID id) {
            super(id);
        }

        @Override
        public void apply(Transaction transaction) {
            transaction.states.add(States.CLOSED);
            transaction.close();
        }
    }

    public States activeState() {
        return states.stream()
                .reduce((previous, current) -> current).get();
    }

    private enum States {
        PIN_VALIDATING,
        DISPENSING,
        DEPOSITING,
        INQUIRING,
        CARD_READING,
        INITIAL,
        CARD_RETAINING,
        WITHDRAWING,
        CLOSED
    }

    public List<States> getStates() {
        return newArrayList(states);
    }

    public Card getCard() {
        return card;
    }

    @Override
    public String toString() {
        return toStringHelper()
                .add("states", states)
                .add("card", card)
                .add("atmInfo", atmInfo)
                .toString();
    }
}

package org.bluebank.banking;

import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.banking.account.command.CreateAccount;
import org.bluebank.banking.account.command.factory.CreateAccountFactory;
import org.bluebank.banking.authorization.command.PerformPinValidation;
import org.bluebank.banking.authorization.command.factory.PerformPinValidationFactory;
import org.bluebank.banking.transaction.command.PerformDeposit;
import org.bluebank.banking.transaction.command.PerformInquiry;
import org.bluebank.banking.transaction.command.PerformWithdraw;
import org.bluebank.banking.transaction.command.factory.PerformDepositFactory;
import org.bluebank.banking.transaction.command.factory.PerformInquiryFactory;
import org.bluebank.banking.transaction.command.factory.PerformWithdrawFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class Bank {
    private final ConcurrentMap<String, UUID> accounts;
    private final CreateAccountFactory createAccountFactory;
    private final PerformDepositFactory performDepositFactory;
    private final PerformWithdrawFactory performWithdrawFactory;
    private final PerformInquiryFactory performInquiryFactory;
    private final PerformPinValidationFactory performPinValidationFactory;
    private final CommandProcessor commandProcessor;

    @Inject
    public Bank(CreateAccountFactory createAccountFactory,
                PerformDepositFactory performDepositFactory,
                PerformWithdrawFactory performWithdrawFactory,
                PerformInquiryFactory performInquiryFactory,
                PerformPinValidationFactory performPinValidationFactory,
                CommandProcessor commandProcessor) {
        this.createAccountFactory = createAccountFactory;
        this.performDepositFactory = performDepositFactory;
        this.performWithdrawFactory = performWithdrawFactory;
        this.performInquiryFactory = performInquiryFactory;
        this.performPinValidationFactory = performPinValidationFactory;
        this.commandProcessor = commandProcessor;
        this.accounts = new ConcurrentHashMap<>();
    }

    public void performDeposit(UUID transactionId, String cardNumber, BigDecimal amount) {
        UUID accountNumber = accounts.get(cardNumber);
        PerformDeposit performDeposit = performDepositFactory.build(transactionId, accountNumber, amount);
        commandProcessor.process(performDeposit);
    }

    public void performWithdraw(UUID transactionId, String cardNumber, BigDecimal amount) {
        UUID accountNumber = accounts.get(cardNumber);
        PerformWithdraw performWithdraw = performWithdrawFactory.build(transactionId, accountNumber, amount);
        commandProcessor.process(performWithdraw);
    }

    public void performInquiry(UUID transactionId, String cardNumber) {
        UUID accountNumber = accounts.get(cardNumber);
        PerformInquiry performInquiry = performInquiryFactory.build(transactionId, accountNumber);
        commandProcessor.process(performInquiry);
    }

    public void performPinValidation(UUID transactionId, String cardNumber, String pinProvided) {
        UUID accountNumber = accounts.get(cardNumber);
        PerformPinValidation performPinValidation = performPinValidationFactory.build(transactionId, accountNumber, pinProvided);
        commandProcessor.process(performPinValidation);
    }

    public void createAccount(UUID transactionId, String cardNumber, String pin) {
        CreateAccount createAccount = createAccountFactory.build(transactionId, cardNumber, pin);
        commandProcessor.process(createAccount);
        accounts.put(cardNumber, transactionId);
    }
}

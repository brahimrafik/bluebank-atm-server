package org.bluebank.bdd;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ServiceManager;
import dagger.Module;
import dagger.Provides;
import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.api.domain.IdGenerator;
import org.bluebank.api.domain.PlatformClock;
import org.bluebank.api.domain.Repository;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.api.endpoint.OutboundEndPoint;
import org.bluebank.api.persistence.AbstractInMemoryRepository;
import org.bluebank.atm.Atm;
import org.bluebank.atm.Message;
import org.bluebank.atm.Transaction;
import org.bluebank.atm.TransactionFactory;
import org.bluebank.atm.authorization.inbound.PinValidationResponseReceiver;
import org.bluebank.atm.authorization.inbound.ValidateCardRequestReceiver;
import org.bluebank.atm.authorization.inbound.ValidatePinRequestReceiver;
import org.bluebank.atm.authorization.outbound.CardValidationRequestSender;
import org.bluebank.atm.authorization.outbound.PinValidationResponseSender;
import org.bluebank.atm.transaction.inbound.DepositRequestReceiver;
import org.bluebank.atm.transaction.inbound.DepositResponseReceiver;
import org.bluebank.atm.transaction.inbound.InquiryRequestReceiver;
import org.bluebank.atm.transaction.inbound.InquiryResponseReceiver;
import org.bluebank.atm.transaction.inbound.WithdrawRequestReceiver;
import org.bluebank.atm.transaction.inbound.WithdrawResponseReceiver;
import org.bluebank.atm.transaction.outbound.ReceiptSender;
import org.bluebank.banking.account.model.Account;
import org.bluebank.banking.account.model.AccountFactory;
import org.bluebank.banking.authorization.outbound.PinValidationResponse;
import org.bluebank.banking.transaction.model.Inquiry;
import org.bluebank.banking.transaction.model.TransactionConfirmation;
import org.bluebank.contract.Messages.CardValidationStatus;
import org.bluebank.contract.Messages.DepositRequest;
import org.bluebank.contract.Messages.InquiryRequest;
import org.bluebank.contract.Messages.Receipt;
import org.bluebank.contract.Messages.ValidateCardRequest;
import org.bluebank.contract.Messages.ValidatePinRequest;
import org.bluebank.contract.Messages.WithdrawRequest;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.UUID;

import static java.lang.Thread.UncaughtExceptionHandler;
import static org.mockito.Mockito.mock;

@Module
public class BddConfigurationModule {

    @Provides
    @Singleton
    public UncaughtExceptionHandler provideUncaughtExceptionHandler() {
        return mock(UncaughtExceptionHandler.class);
    }

    @Singleton
    @Provides
    public Repository<Account> provideAccountRepository(AccountFactory accountFactory) {
        return new AbstractInMemoryRepository<Account>() {
            @Override
            public Account create(UUID id) {
                return accountFactory.build(id);
            }
        };
    }

    @Singleton
    @Provides
    public EventBus provideEventBus() {
        return new EventBus();
    }

    @Singleton
    @Provides
    public Repository<Transaction> provideRepository(TransactionFactory transactionFactory) {
        return new AbstractInMemoryRepository<Transaction>() {
            @Override
            public Transaction create(UUID id) {
                return transactionFactory.build(id);
            }
        };
    }

    @Singleton
    @Provides
    @Named("validateCardRequestReceiver")
    public InboundEndPoint<ValidateCardRequest> provideValidateCardRequestReceiver(ValidateCardRequestReceiver validateCardRequestReceiver) {
        return validateCardRequestReceiver;
    }

    @Singleton
    @Provides
    @Named("validatePinRequestReceiver")
    public InboundEndPoint<ValidatePinRequest> provideValidatePinRequestReceiver(ValidatePinRequestReceiver validatePinRequestReceiver) {
        return validatePinRequestReceiver;
    }

    @Singleton
    @Provides
    @Named("depositRequestReceiver")
    public InboundEndPoint<DepositRequest> provideDepositRequestReceiver(DepositRequestReceiver depositRequestReceiver) {
        return depositRequestReceiver;
    }

    @Singleton
    @Provides
    @Named("withdrawRequestReceiver")
    public InboundEndPoint<WithdrawRequest> provideWithdrawRequestReceiver(WithdrawRequestReceiver withdrawRequestReceiver) {
        return withdrawRequestReceiver;
    }

    @Singleton
    @Provides
    @Named("inquiryRequestReceiver")
    public InboundEndPoint<InquiryRequest> provideInquiryRequestReceiver(InquiryRequestReceiver inquiryRequestReceiver) {
        return inquiryRequestReceiver;
    }

    @Singleton
    @Provides
    @Named("depositResponseReceiver")
    public InboundEndPoint<TransactionConfirmation> provideDepositResponseReceiver(DepositResponseReceiver depositResponseReceiver) {
        return depositResponseReceiver;
    }

    @Singleton
    @Provides
    @Named("withdrawResponseReceiver")
    public InboundEndPoint<TransactionConfirmation> provideWithdrawResponseReceiver(WithdrawResponseReceiver withdrawResponseReceiver) {
        return withdrawResponseReceiver;
    }

    @Singleton
    @Provides
    @Named("inquiryResponseReceiver")
    public InboundEndPoint<Inquiry> provideInquiryResponseReceiver(InquiryResponseReceiver inquiryResponseReceiver) {
        return inquiryResponseReceiver;
    }

    @Singleton
    @Provides
    @Named("pinValidationResponseReceiver")
    public InboundEndPoint<PinValidationResponse> providePinValidationResponseReceiver(PinValidationResponseReceiver pinValidationResponseReceiver) {
        return pinValidationResponseReceiver;
    }

    @Singleton
    @Provides
    @Named("cardValidationRequestSender")
    public OutboundEndPoint<Message> provideCardValidationRequestSender() {
        return mock(CardValidationRequestSender.class);
    }

    @Singleton
    @Provides
    @Named("pinValidationResponseSender")
    public OutboundEndPoint<CardValidationStatus> providePinValidationResponseSender() {
        return mock(PinValidationResponseSender.class);
    }

    @Singleton
    @Provides
    @Named("receiptSender")
    public OutboundEndPoint<Receipt> provideReceiptSender() {
        return mock(ReceiptSender.class);
    }


    @Singleton
    @Provides
    public IdGenerator provideIdGenerator() {
        return mock(IdGenerator.class);
    }

    @Singleton
    @Provides
    public PlatformClock providePlatformClock() {
        return mock(PlatformClock.class);
    }

    @Provides
    @Singleton
    public Atm provideAtmMachine() {
        return mock(Atm.class);
    }

    @Provides
    @Singleton
    public CommandProcessor provideCommandController() {
        return command -> {
            command.execute();
            return true;
        };
    }

    @Provides
    @Singleton
    public ServiceManager getServiceManager() {
        return mock(ServiceManager.class);
    }

    @Provides
    @Singleton
    public HealthCheckRegistry provideHealthCheckRegistry() {
        return mock(HealthCheckRegistry.class);
    }

    @Provides
    @Singleton
    public MetricRegistry provideMetricRegistry() {
        return mock(MetricRegistry.class);
    }

    @Provides
    @Singleton
    public ThreadDeadlockHealthCheck provideThreadDeadlockHealthCheck() {
        return mock(ThreadDeadlockHealthCheck.class);
    }

}

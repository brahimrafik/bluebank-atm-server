package org.bluebank;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ServiceManager;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dagger.Module;
import dagger.Provides;
import org.apache.commons.configuration.Configuration;
import org.bluebank.api.ServiceManagerProvider;
import org.bluebank.api.command.processor.CommandController;
import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.api.command.service.CommandControllerService;
import org.bluebank.api.domain.Repository;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.api.endpoint.OutboundEndPoint;
import org.bluebank.api.persistence.AbstractInMemoryRepository;
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
import org.bluebank.resource.TransactionResource;

import javax.inject.Named;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Module
public class AtmModule {

    @Provides
    @Singleton
    public ListeningExecutorService provideListeningExecutorService(ThreadFactory threadFactory) {
        return MoreExecutors.listeningDecorator(newFixedThreadPool(4, threadFactory));
    }

    @Provides
    @Singleton
    public ThreadFactory provideThreadFactory() {
        return new ThreadFactoryBuilder()
                .setNameFormat("executor-service-%d")
                .build();
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
    public EventBus provideEventBus(ListeningExecutorService executorService) {
        return new AsyncEventBus("eventbus", executorService);
    }

    @Singleton
    @Provides
    @Named("cardValidationRequestSender")
    public OutboundEndPoint<Message> provideCardValidationRequestSender(CardValidationRequestSender cardValidationRequestSender) {
        return cardValidationRequestSender;
    }

    @Singleton
    @Provides
    @Named("pinValidationResponseSender")
    public OutboundEndPoint<CardValidationStatus> providePinReadSender(PinValidationResponseSender pinValidationResponseSender) {
        return pinValidationResponseSender;
    }

    @Singleton
    @Provides
    @Named("receiptSender")
    public OutboundEndPoint<Receipt> provideReceiptSender(ReceiptSender receiptSender) {
        return receiptSender;
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

    @Provides
    @Singleton
    public TransactionResource provideTransactionResource(Repository<Transaction> repository) {
        return new TransactionResource(repository);
    }

    @Provides
    @Singleton
    public CommandProcessor provideCommandProcessor(CommandController commandController) {
        return commandController;
    }

    @Provides
    @Singleton
    public HealthCheckRegistry provideHealthCheckRegistry() {
        return new HealthCheckRegistry();
    }

    @Provides
    @Singleton
    public MetricRegistry provideMetricRegistry() {
        return new MetricRegistry();
    }

    @Provides
    @Singleton
    public ServiceManager provideServiceManager(HealthCheckRegistry healthCheckRegistry, CommandControllerService controllerService) {
        return new ServiceManagerProvider(healthCheckRegistry, controllerService).get();
    }

    @Provides
    @Singleton
    public ThreadDeadlockHealthCheck provideThreadDeadlockHealthCheck(HealthCheckRegistry healthCheckRegistry) {
        ThreadDeadlockHealthCheck threadDeadlockHealthCheck = new ThreadDeadlockHealthCheck();
        healthCheckRegistry.register("Deadlock", threadDeadlockHealthCheck);
        return threadDeadlockHealthCheck;
    }

    @Provides
    @Singleton
    public ControllerConfiguration provideControllerConfiguration(@Named("atm.properties") Configuration configuration) {
        return new ControllerConfiguration() {
            @Override
            public int getTimeout() {
                return configuration.getInt("controller.timeout");
            }


            @Override
            public int getCapacity() {
                return configuration.getInt("controller.capacity");
            }
        };
    }

    public interface ControllerConfiguration {
        int getTimeout();

        int getCapacity();
    }

    @Provides
    @Singleton
    public ATMConfiguration provideATMConfiguration(@Named("atm.properties") Configuration configuration) {
        return () -> configuration.getBigDecimal("atm.cashonhand");
    }

    public interface ATMConfiguration {
        BigDecimal getCashOnHand();
    }
}

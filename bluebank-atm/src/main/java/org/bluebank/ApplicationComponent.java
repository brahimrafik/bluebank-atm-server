package org.bluebank;


import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.google.common.util.concurrent.ServiceManager;
import dagger.Component;
import org.bluebank.api.domain.Repository;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.authorization.outbound.CardValidationRequestedHandler;
import org.bluebank.atm.authorization.outbound.PinValidationRequestHandler;
import org.bluebank.atm.authorization.outbound.PinValidationResponseHandler;
import org.bluebank.atm.transaction.outbound.DepositRequestedHandler;
import org.bluebank.atm.transaction.outbound.InquiryRequestedHandler;
import org.bluebank.atm.transaction.outbound.WithdrawRequestedHandler;
import org.bluebank.banking.Bank;
import org.bluebank.banking.account.model.Account;
import org.bluebank.banking.authorization.outbound.PinValidationHandler;
import org.bluebank.banking.transaction.outbound.DepositPerformedHandler;
import org.bluebank.banking.transaction.outbound.InquiryPerformedHandler;
import org.bluebank.banking.transaction.outbound.WithdrawPerformedHandler;
import org.bluebank.contract.Messages.DepositRequest;
import org.bluebank.contract.Messages.InquiryRequest;
import org.bluebank.contract.Messages.ValidateCardRequest;
import org.bluebank.contract.Messages.ValidatePinRequest;
import org.bluebank.contract.Messages.WithdrawRequest;
import org.bluebank.resource.AccountResource;
import org.bluebank.resource.AtmResource;
import org.bluebank.resource.TransactionResource;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ConfigurationModule.class, AtmModule.class, BankModule.class})
public interface ApplicationComponent {

    Repository<Account> getAccountRepository();

    @Named("validateCardRequestReceiver")
    InboundEndPoint<ValidateCardRequest> getValidateCardRequestReceiver();

    @Named("validatePinRequestReceiver")
    InboundEndPoint<ValidatePinRequest> getValidatePinRequestReceiver();

    CardValidationRequestedHandler getCardValidationRequestedHandler();

    PinValidationResponseHandler getPinValidationResponseHandler();

    DepositRequestedHandler getDepositRequestedHandler();

    WithdrawRequestedHandler getWithdrawRequestedHandler();

    @Named("depositRequestReceiver")
    InboundEndPoint<DepositRequest> getDepositRequestReceiver();

    @Named("withdrawRequestReceiver")
    InboundEndPoint<WithdrawRequest> getWithdrawRequestReceiver();

    @Named("inquiryRequestReceiver")
    InboundEndPoint<InquiryRequest> getInquiryRequestReceiver();

    InquiryRequestedHandler getInquiryRequestedHandler();

    TransactionResource getTransactionResource();

    AccountResource getAccountResource();

    PinValidationRequestHandler getPinValidationRequestHandler();

    DepositPerformedHandler getDepositPerformedHandler();

    WithdrawPerformedHandler getWithdrawPerformedHandler();

    InquiryPerformedHandler getInquiryPerformedHandler();

    PinValidationHandler getPinValidationHandler();

    ServiceManager getServiceManager();

    HealthCheckRegistry getHealthCheckRegistry();

    MetricRegistry getMetricRegistry();

    ThreadDeadlockHealthCheck getThreadDeadlockHealthCheck();

    Bank getBank();

    AtmResource getAtmResource();
}

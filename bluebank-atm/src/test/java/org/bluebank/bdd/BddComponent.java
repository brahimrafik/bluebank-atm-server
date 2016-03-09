package org.bluebank.bdd;

import dagger.Component;
import org.bluebank.ApplicationComponent;
import org.bluebank.api.domain.IdGenerator;
import org.bluebank.api.domain.PlatformClock;
import org.bluebank.api.domain.Repository;
import org.bluebank.api.endpoint.OutboundEndPoint;
import org.bluebank.atm.Atm;
import org.bluebank.atm.Message;
import org.bluebank.banking.Bank;
import org.bluebank.banking.account.model.Account;
import org.bluebank.contract.Messages.CardValidationStatus;
import org.bluebank.contract.Messages.Receipt;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Component(modules = BddConfigurationModule.class)
public interface BddComponent extends ApplicationComponent {

    @Named("pinValidationResponseSender")
    OutboundEndPoint<CardValidationStatus> getPinValidationResponseSender();

    @Named("cardValidationRequestSender")
    OutboundEndPoint<Message> getCardValidationRequestSender();

    @Named("receiptSender")
    OutboundEndPoint<Receipt> getReceiptSender();

    Repository<Account> getAccountRepository();

    Bank getBank();

    IdGenerator getIdGenerator();

    PlatformClock getPlatformClock();

    Atm getAtm();
}

package org.bluebank.banking.transaction.command.factory;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.Repository;
import org.bluebank.banking.account.model.Account;
import org.bluebank.banking.transaction.command.PerformInquiry;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class PerformInquiryFactory {
    private final Repository<Account> repository;
    private final DomainEventsDispatcher eventsDispatcher;

    @Inject
    public PerformInquiryFactory(
            Repository<Account> repository,
            DomainEventsDispatcher eventsDispatcher) {
        this.repository = repository;
        this.eventsDispatcher = eventsDispatcher;
    }

    public PerformInquiry build(UUID atmTransaction, UUID id) {
        return new PerformInquiry(id, repository, eventsDispatcher, atmTransaction);
    }
}

package org.bluebank;

import dagger.Module;
import dagger.Provides;
import org.bluebank.api.domain.Repository;
import org.bluebank.api.persistence.AbstractInMemoryRepository;
import org.bluebank.banking.account.model.Account;
import org.bluebank.banking.account.model.AccountFactory;
import org.bluebank.resource.AccountResource;

import javax.inject.Singleton;
import java.util.UUID;

@Module
public class BankModule {

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

    @Provides
    @Singleton
    public AccountResource provideAccountResource(Repository<Account> repository) {
        return new AccountResource(repository);
    }

}


package org.bluebank.banking.account.model;

import org.bluebank.api.domain.IdGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class AccountFactory {
    private final IdGenerator idGenerator;

    @Inject
    public AccountFactory(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Account build(UUID id) {
        return new Account(id, idGenerator);
    }
}

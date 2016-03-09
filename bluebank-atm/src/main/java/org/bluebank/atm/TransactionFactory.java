package org.bluebank.atm;

import org.bluebank.api.domain.AggregateRootFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class TransactionFactory extends AggregateRootFactory<Transaction> {

    @Inject
    public TransactionFactory() {
    }

    public Transaction build(UUID id) {
        return new Transaction(id);
    }
}

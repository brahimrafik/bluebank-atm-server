package org.bluebank.api.persistence;

import org.bluebank.api.domain.AggregateRoot;
import org.bluebank.api.domain.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Optional.ofNullable;

public abstract class AbstractInMemoryRepository<T extends AggregateRoot> implements Repository<T> {
    private final ConcurrentMap<UUID, T> transactions = new ConcurrentHashMap<>();

    @Override
    public Optional<T> load(UUID id) {
        return ofNullable(transactions.get(id));
    }

    @Override
    public void save(T transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    @Override
    public Collection<T> all() {
        return transactions.values();
    }

    @Override
    public abstract T create(UUID id);
}

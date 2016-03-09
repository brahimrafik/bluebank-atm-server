package org.bluebank.api.domain;

import java.util.UUID;

public abstract class DomainEvent<T extends AggregateRoot> {
    public final UUID id;

    public DomainEvent(UUID id) {
        this.id = id;
    }

    public abstract void apply(T t);
}

package org.bluebank.api;

import org.bluebank.api.domain.AggregateRoot;

import java.util.UUID;

public class DummyAggregate extends AggregateRoot {

    @Override
    public UUID getId() {
        return new UUID(0, 1);
    }
}

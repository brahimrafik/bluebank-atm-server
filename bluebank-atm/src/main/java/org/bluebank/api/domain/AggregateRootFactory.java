package org.bluebank.api.domain;

import java.util.UUID;

public abstract class AggregateRootFactory<T extends AggregateRoot> {

    public abstract T build(UUID id);

}

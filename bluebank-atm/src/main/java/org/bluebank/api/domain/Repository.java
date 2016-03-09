package org.bluebank.api.domain;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Repository< T extends AggregateRoot> {

    Optional<T> load(UUID id);

    void save(T t);

    T create(UUID id);

    Collection<T> all();
}

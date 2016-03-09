package org.bluebank.api.command;

import org.bluebank.api.bus.DomainEventsDispatcher;
import org.bluebank.api.domain.AggregateRoot;
import org.bluebank.api.domain.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public abstract class AggregateRootCommand<T extends AggregateRoot> implements Command {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UUID aggregateRootId;
    private final Repository<T> repository;
    private final DomainEventsDispatcher domainEventsDispatcher;

    protected AggregateRootCommand(UUID aggregateRootId,
                                   Repository<T> repository,
                                   DomainEventsDispatcher domainEventsDispatcher) {
        this.aggregateRootId = aggregateRootId;
        this.repository = repository;
        this.domainEventsDispatcher = domainEventsDispatcher;
    }

    @Override
    public void execute() {
        Optional<T> optional = repository.load(aggregateRootId);
        if (optional.isPresent()) {
            T aggregateRoot = optional.get();
            delegateToAggregateRoot(aggregateRoot);
            repository.save(aggregateRoot);
            domainEventsDispatcher.apply(aggregateRoot);
        } else {
            logger.info("unknown aggregate id {}, command will be ignored", aggregateRootId);
        }
    }

    public abstract void delegateToAggregateRoot(T aggregateRoot);

}

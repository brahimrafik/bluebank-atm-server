package org.bluebank.api.bus;


import com.google.common.eventbus.EventBus;
import org.bluebank.api.domain.AggregateRoot;
import org.bluebank.api.domain.DomainEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Singleton
public class DomainEventsDispatcher {
    private final EventBus eventBus;

    @Inject
    public DomainEventsDispatcher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void apply(AggregateRoot aggregateRoot) {
        List<DomainEvent> uncommittedEvents = newArrayList(aggregateRoot.getUncommittedEvents());
        aggregateRoot.clearUncommittedEvents();
        uncommittedEvents.stream().forEach(eventBus::post);
    }
}

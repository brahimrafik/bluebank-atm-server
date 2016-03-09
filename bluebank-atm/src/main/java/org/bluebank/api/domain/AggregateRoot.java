package org.bluebank.api.domain;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.MoreObjects.ToStringHelper;
import static com.google.common.collect.ImmutableList.copyOf;

public abstract class AggregateRoot implements Entity {
    private final List<DomainEvent> uncommittedEvents;
    private boolean closed;
    private int version;

    protected AggregateRoot() {
        uncommittedEvents = new ArrayList<>();
        closed = false;
        version = 1;
    }

    protected void applyEvent(DomainEvent event) {
        event.apply(this);
        uncommittedEvents.add(event);
        version++;
    }

    public final List<DomainEvent> getUncommittedEvents() {
        return copyOf(uncommittedEvents);
    }

    public final void clearUncommittedEvents() {
        uncommittedEvents.clear();
    }

    public final int getVersion() {
        return version;
    }

    public final void close() {
        closed = true;
    }

    public final boolean isClosed() {
        return closed;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AggregateRoot that = (AggregateRoot) obj;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    protected ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("uncommittedEvents", uncommittedEvents)
                .add("closed", closed)
                .add("version", version);
    }


}


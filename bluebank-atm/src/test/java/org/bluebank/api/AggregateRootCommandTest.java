package org.bluebank.api;

import org.bluebank.api.command.AggregateRootCommand;
import org.bluebank.api.domain.Repository;
import org.bluebank.api.bus.DomainEventsDispatcher;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AggregateRootCommandTest {

    private DomainEventsDispatcher domainEventsDispatcher;
    private Repository<DummyAggregate> repository;

    @Before
    public void before() {
        domainEventsDispatcher = mock(DomainEventsDispatcher.class);
        repository = mock(Repository.class);
    }

    @Test
    public void should_execute_aggregate_command() {
        // given
        DummyAggregate dummyAggregate = new DummyAggregate();
        when(repository.load(dummyAggregate.getId())).thenReturn(Optional.of(dummyAggregate));
        DummyAggregateRootCommand dummyAggregateRootCommand = new DummyAggregateRootCommand(
                dummyAggregate.getId(),
                repository,
                domainEventsDispatcher
        );

        // when
        dummyAggregateRootCommand.execute();

        // then
        verify(repository).save(dummyAggregate);
        verify(domainEventsDispatcher).apply(dummyAggregate);

    }

    @Test
    public void should_failed_to_execute_unknown_aggregate_command() {
        // given
        DummyAggregate dummyAggregate = new DummyAggregate();
        when(repository.load(dummyAggregate.getId())).thenReturn(Optional.empty());
        DummyAggregateRootCommand dummyAggregateRootCommand = new DummyAggregateRootCommand(
                dummyAggregate.getId(),
                repository,
                domainEventsDispatcher
        );

        // when
        dummyAggregateRootCommand.execute();

        // then
        verify(repository, never()).save(dummyAggregate);
        verify(domainEventsDispatcher, never()).apply(dummyAggregate);

    }

    private class DummyAggregateRootCommand extends AggregateRootCommand<DummyAggregate> {

        protected DummyAggregateRootCommand(UUID aggregateRootId,
                                            Repository<DummyAggregate> repository,
                                            DomainEventsDispatcher domainEventsDispatcher) {
            super(aggregateRootId, repository, domainEventsDispatcher);
        }

        @Override
        public void delegateToAggregateRoot(DummyAggregate aggregateRoot) {

        }
    }

}
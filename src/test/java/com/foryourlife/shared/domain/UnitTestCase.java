package com.foryourlife.shared.domain;

import com.foryourlife.shared.domain.bus.DomainEvent;
import com.foryourlife.shared.domain.bus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

public class UnitTestCase {
    protected EventBus eventBus;

    @BeforeEach
    protected void setUp() {
        eventBus = Mockito.mock(EventBus.class);
    }


    public void shouldHavePublished(List<DomainEvent> domainEvents) {
        Mockito.verify(eventBus, Mockito.atLeastOnce()).publish(domainEvents);
    }

    public void shouldHavePublished(DomainEvent domainEvent) {
        shouldHavePublished(Collections.singletonList(domainEvent));
    }
}

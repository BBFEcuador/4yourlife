package com.foryourlife.shared.domain.bus;

import java.util.List;

public interface EventBus {
    void publish(final List<DomainEvent> events);
}

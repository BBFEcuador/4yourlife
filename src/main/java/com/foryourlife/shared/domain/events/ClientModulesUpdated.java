package com.foryourlife.shared.domain.events;

import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.util.HashMap;

public class ClientModulesUpdated extends DomainEvent {
    private final ClientModule clientModule;

    public ClientModulesUpdated(ClientModule clientModule) {
        this.clientModule = clientModule;
    }

    @Override
    public String eventName() {
        return "";
    }

    @Override
    public HashMap<String, Serializable> toPrimitives() {
        return null;
    }

    @Override
    public DomainEvent fromPrimitives(String aggregateId, HashMap<String, Serializable> body, String eventId, String occurredOn) {
        return null;
    }

    public ClientModule getClientModule() {
        return clientModule;
    }
}

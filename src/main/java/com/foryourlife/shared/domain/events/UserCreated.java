package com.foryourlife.shared.domain.events;

import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class UserCreated extends DomainEvent {

    private final Participant participant;

    public UserCreated(String aggregateId, Participant user) {
        super(
                aggregateId,
                UUID.randomUUID().toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
        );
        this.participant = user;
    }
    public Participant getParticipant() {
        return participant;
    }
    @Override
    public String eventName() {
        return "account.users.created";
    }

    @Override
    public HashMap<String, Serializable> toPrimitives() {
        return null;
    }

    @Override
    public DomainEvent fromPrimitives(String aggregateId, HashMap<String, Serializable> body, String eventId, String occurredOn) {
        return null;
    }
}

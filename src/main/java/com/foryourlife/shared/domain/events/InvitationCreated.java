package com.foryourlife.shared.domain.events;

import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class InvitationCreated extends DomainEvent {

    private final Invitation invitation;

    public InvitationCreated(String aggregateId, Invitation invitation) {
        super(aggregateId, UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        this.invitation = invitation;
    }

    public Invitation getInvitation() {
        return invitation;
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
}

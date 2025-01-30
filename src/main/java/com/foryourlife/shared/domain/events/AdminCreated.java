package com.foryourlife.shared.domain.events;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class AdminCreated extends DomainEvent {
    private Admin admin;
    private String plainPassword;
    public AdminCreated(String aggregateId, Admin admin, String plainPassword) {
        super(aggregateId, UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        this.admin = admin;
        this.plainPassword = plainPassword;
    }

    public Admin getAdmin() {
        return admin;
    }

    public String getPlainPassword() {
        return plainPassword;
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

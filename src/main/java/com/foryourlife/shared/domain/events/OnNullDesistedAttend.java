package com.foryourlife.shared.domain.events;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Users;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class OnNullDesistedAttend extends DomainEvent {
    private final Users user;
    private final Training training;

    public OnNullDesistedAttend(String aggregateId, Users user, Training training) {
        super(
                aggregateId,
                UUID.randomUUID().toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
        );
        this.user = user;
        this.training = training;
    }

    public Users getUser() {
        return user;
    }

    public Training getTraining() {
        return training;
    }

    @Override
    public String eventName() {
        return "programs.attend.nullAttend";
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

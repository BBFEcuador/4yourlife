package com.foryourlife.shared.domain.events;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class TeamCreated extends DomainEvent {
    private final Team team;

    public TeamCreated(String aggregateId, Team team) {
        super(
                aggregateId,
                UUID.randomUUID().toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
        );
        this.team = team;
    }
    public Team getTeam() {
        return team;
    }
    @Override
    public String eventName() {
        return "programs.team.created";
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

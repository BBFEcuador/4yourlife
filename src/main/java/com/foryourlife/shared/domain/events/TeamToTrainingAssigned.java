package com.foryourlife.shared.domain.events;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class TeamToTrainingAssigned extends DomainEvent {
    private final Team team;
    private final Training training;

    public TeamToTrainingAssigned(String aggregateId, Team team, Training training) {
        super(
                aggregateId,
                UUID.randomUUID().toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
        );
        this.team = team;
        this.training = training;
    }

    public Team getTeam() {
        return team;
    }

    public Training getTraining() {
        return training;
    }

    @Override
    public String eventName() {
        return "programs.team.assigned";
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

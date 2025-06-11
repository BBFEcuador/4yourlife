package com.foryourlife.admin.programs.teams.infrastructure.httpControllers;

import com.foryourlife.clients.account.participant.domain.Participant;

import java.util.Set;

public class AssignParticipantsRequest {

    private String teamId;
    private Set<Participant> users;

    public AssignParticipantsRequest(String teamId, Set<Participant> users) {
        this.teamId = teamId;
        this.users = users;
    }

    public String getTeamId() {
        return teamId;
    }

    public Set<Participant> getUsers() {
        return users;
    }
}

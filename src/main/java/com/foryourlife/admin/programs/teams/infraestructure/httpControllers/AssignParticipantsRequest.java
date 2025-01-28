package com.foryourlife.admin.programs.teams.infraestructure.httpControllers;

import com.foryourlife.clients.account.user.domain.Users;

import java.util.Set;

public class AssignParticipantsRequest {

    private String teamId;
    private Set<Users> users;

    public AssignParticipantsRequest(String teamId, Set<Users> users) {
        this.teamId = teamId;
        this.users = users;
    }

    public String getTeamId() {
        return teamId;
    }

    public Set<Users> getUsers() {
        return users;
    }
}

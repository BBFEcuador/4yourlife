package com.foryourlife.admin.programs.teams.infraestructure.httpControllers.request;

import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.masterLife.domain.MasterLife;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class SaveLifeTeamRequest {
    public String id;
    @NotNull
    public String name;
    @NotNull
    public String training;
    @NotNull
    @Size(min = 1)
    public List<Participant> users;
    @NotNull
    @Size(min = 1)
    public List<MasterLife> masterLife;
    @NotNull
    public String trainer;
}

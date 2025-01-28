package com.foryourlife.admin.programs.teams.infraestructure;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

public class SaveTeamRequest {
    @Id
    public String id;
    @NotNull
    public String name;
    public String photo;
    @NotNull
    public String trainingId;
}

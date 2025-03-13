package com.foryourlife.admin.programs.teams.infraestructure.httpControllers.request;

import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.staff.domain.Staff;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class SaveYourTeamRequest {
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
    public List<Staff> staffs;
    @NotNull
    public String trainer;
}

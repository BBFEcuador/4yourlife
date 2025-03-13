package com.foryourlife.admin.programs.teams.infraestructure.httpControllers.request;

import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.staff.domain.Staff;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PromotionYourRequest {
    public String id;
    @NotNull
    @Size(min = 1)
    public List<Participant> users;
    @NotNull
    @Size(min = 1)
    public List<Staff> staffs;
    @NotNull
    public String trainer;
}

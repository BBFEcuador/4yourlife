package com.foryourlife.admin.programs.teams.infraestructure.httpControllers.request;

import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.masterLife.domain.MasterLife;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PromotionLifeRequest {
    @NotNull
    public String id;
    @NotNull
    @Size(min = 1,message = "Minimo 1 participante")
    public List<Participant> users;
    @NotNull
    @Size(min = 1,message = "Minimo 1 master life")
    public List<MasterLife> masterLife;
    @NotNull(message = "Debe seleccionar un entrenador")
    public String trainer;
    public String name;
}

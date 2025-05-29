package com.foryourlife.clients.account.module.infrastructure.httpControllers;

import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.participant.domain.Participant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ClientModuleRequest {
    @NotNull
    @NotBlank
    private String id;

    @NotNull
    private Boolean hasFocus;

    @NotNull
    private Boolean hasYour;

    @NotNull
    private Boolean hasLife;

    private Participant userId;

    public ClientModuleRequest(String id, Boolean hasFocus, Boolean hasYour, Boolean hasLife, Participant userId) {
        this.id = id;
        this.hasFocus = hasFocus;
        this.hasYour = hasYour;
        this.hasLife = hasLife;
        this.userId = userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHasFocus(Boolean hasFocus) {
        this.hasFocus = hasFocus;
    }

    public void setHasYour(Boolean hasYour) {
        this.hasYour = hasYour;
    }

    public void setHasLife(Boolean hasLife) {
        this.hasLife = hasLife;
    }

    public void setUserId(Participant userId) {
        this.userId = userId;
    }

    public ClientModule toDomain(){
        return ClientModule.create(id, hasFocus, hasYour, hasLife, userId);
    }
}

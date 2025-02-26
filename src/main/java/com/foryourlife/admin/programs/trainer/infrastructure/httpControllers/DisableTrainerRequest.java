package com.foryourlife.admin.programs.trainer.infrastructure.httpControllers;

import jakarta.validation.constraints.NotNull;

public class DisableTrainerRequest {
    public String id;
    @NotNull
    public boolean isActive;

    public String getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }
}

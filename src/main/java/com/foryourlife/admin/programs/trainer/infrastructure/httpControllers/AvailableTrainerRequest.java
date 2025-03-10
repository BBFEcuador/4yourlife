package com.foryourlife.admin.programs.trainer.infrastructure.httpControllers;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AvailableTrainerRequest {
    @NotNull
    public LocalDate startDate;
    @NotNull
    public LocalDate endDate;

}

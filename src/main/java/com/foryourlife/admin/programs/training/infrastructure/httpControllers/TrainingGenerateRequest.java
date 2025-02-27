package com.foryourlife.admin.programs.training.infrastructure.httpControllers;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TrainingGenerateRequest {
    @NotNull
    public LocalDate startDate;
    @NotNull
    public Integer numberOfFocus;
    @NotNull
    public String campusId;

}

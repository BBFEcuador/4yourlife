package com.foryourlife.admin.programs.training.infrastructure;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TrainingAutoGenerateRequest {
    @NotNull
    public LocalDate startDate;
    @NotNull
    public Integer firstFocus;
    @NotNull
    public Integer numberOfFocus;
    @NotNull
    public String campusId;

}

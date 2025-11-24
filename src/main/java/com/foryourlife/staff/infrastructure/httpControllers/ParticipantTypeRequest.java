package com.foryourlife.staff.infrastructure.httpControllers;

import jakarta.validation.constraints.NotNull;

public class ParticipantTypeRequest {
    @NotNull
    public String userId;
    public String role;
}

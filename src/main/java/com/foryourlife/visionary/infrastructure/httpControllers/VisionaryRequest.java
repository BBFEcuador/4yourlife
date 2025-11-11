package com.foryourlife.visionary.infrastructure.httpControllers;

import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.shared.domain.user.infrastructure.SaveGeneraUserRequest;
import com.foryourlife.visionary.domain.Visionary;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class VisionaryRequest {
    public String id;
    @NotNull
    private Boolean active;
    @NotNull
    @Valid
    public SaveGeneraUserRequest user;

    public String getId() {
        return id;
    }

    public Boolean getActive() {
        return active;
    }

    public SaveGeneraUserRequest getUser() {
        return user;
    }

    public Visionary toDomain() {
        var newId = id != null ? id : UUID.randomUUID().toString();
        return Visionary.create(newId, active, user.toDomain(List.of(new UserEntities(newId, UserType.VISIONARY.toString()))));
    }
}

package com.foryourlife.visionary.infrastructure.httpControllers;

import com.foryourlife.shared.domain.user.User;
import com.foryourlife.visionary.domain.Visionary;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class VisionaryUserRequest {
    public String id;
    @NotNull
    public Boolean isActive;
    @NotNull
    public User user;
    public Visionary toDomain(){
        var newId = id != null ? id : UUID.randomUUID().toString();
        return Visionary.create(newId, isActive,user);
    }
}

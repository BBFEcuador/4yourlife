package com.foryourlife.visionary.infrastructure.httpControllers;

import com.foryourlife.shared.domain.user.User;
import com.foryourlife.visionary.domain.Visionary;
import jakarta.validation.constraints.NotNull;

public class VisionaryRequest {
    public String id;
    @NotNull
    public String role;
    @NotNull
    public User user;

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }

    public Visionary toDomain() {
        return Visionary.create(id, role, user);
    }
}

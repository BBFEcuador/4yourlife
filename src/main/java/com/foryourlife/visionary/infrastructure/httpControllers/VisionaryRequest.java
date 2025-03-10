package com.foryourlife.visionary.infrastructure.httpControllers;

import com.foryourlife.shared.domain.user.User;
import com.foryourlife.visionary.domain.Visionary;

public class VisionaryRequest {
    private String id;
    private String role;
    private User user;

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

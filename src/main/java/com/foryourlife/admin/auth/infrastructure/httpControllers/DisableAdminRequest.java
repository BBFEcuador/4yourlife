package com.foryourlife.admin.auth.infrastructure.httpControllers;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class DisableAdminRequest {
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

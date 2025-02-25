package com.foryourlife.admin.auth.infrastructure.httpControllers;

import com.foryourlife.admin.auth.domain.AdminRole;
import com.foryourlife.admin.programs.campus.domain.Campus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Set;

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

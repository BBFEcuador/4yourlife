package com.foryourlife.admin.auth.infrastructure.httpControllers;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UpdatePassAdminRequest {
    @NotNull
    public String id;
    @NotNull
    public String password;


}

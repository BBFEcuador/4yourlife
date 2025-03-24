package com.foryourlife.staff.infrastructure.httpControllers;

import com.foryourlife.shared.domain.user.User;
import com.foryourlife.staff.domain.Staff;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class StaffUserRequest {
    public String id;
    @NotNull
    public String rol;
    @NotNull
    public Boolean isActive;
    @NotNull
    public User user;
    public Staff toDomain(){
        var newId = id != null ? id : UUID.randomUUID().toString();
        return Staff.create(newId, rol, isActive,user);
    }
}

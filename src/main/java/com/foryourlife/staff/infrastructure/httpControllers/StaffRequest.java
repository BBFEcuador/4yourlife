package com.foryourlife.staff.infrastructure.httpControllers;

import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.shared.domain.user.infrastructure.SaveGeneraUserRequest;
import com.foryourlife.staff.domain.Staff;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class StaffRequest {
    public String id;
    @NotNull
    public String rol;
    @NotNull
    @Valid
    public SaveGeneraUserRequest user;
    public Staff toDomain(){
        var newId = id != null ? id : UUID.randomUUID().toString();
        return Staff.create(newId, rol, user.toDomain(List.of(new UserEntities(newId, UserType.STAFF.toString()))));
    }
}

package com.foryourlife.masterLife.infrastructure.httpController;

import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.shared.domain.user.infrastructure.SaveGeneraUserRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class MasterLifeSaveRequest {
    public String id;
    @NotNull
    public Boolean active;
    @NotNull
    @Valid
    public SaveGeneraUserRequest user;
    public MasterLife toDomain() {
        var newId = id != null ? id : UUID.randomUUID().toString();
        return MasterLife.create(newId, active, user.toDomain(List.of(new UserEntities(newId, UserType.MASTER_LIFE.toString()))));
    }
}

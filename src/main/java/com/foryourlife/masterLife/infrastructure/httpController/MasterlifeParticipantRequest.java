package com.foryourlife.masterLife.infrastructure.httpController;

import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.staff.domain.Staff;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class MasterlifeParticipantRequest {
    public String id;
    @NotNull
    public Boolean isActive;
    @NotNull
    public User user;
    public MasterLife toDomain(){
        var newId = id != null ? id : UUID.randomUUID().toString();
        return MasterLife.create(newId, isActive,user);
    }
}

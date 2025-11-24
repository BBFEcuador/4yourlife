package com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class StaffNodeRequest {
    private String id;
    @NotNull(message = "El usuario staff es requerido")
    private String userId;
    @NotNull(message = "Los participantes son requeridos")
    private List<String> participantsIds;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getParticipantsIds() {
        return participantsIds;
    }

    public void setParticipantsIds(List<String> participantsIds) {
        this.participantsIds = participantsIds;
    }
}

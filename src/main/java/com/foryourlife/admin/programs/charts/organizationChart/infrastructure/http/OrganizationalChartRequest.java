package com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrganizationalChartRequest {
    private String id;
    @NotNull(message = "El equipo es requerido")
    private String teamId;
    private List<MasterLifeNodeRequest> masterLives;
    @Valid
    private List<VisionaryNodeRequest> visionaries;
    @Valid
    private List<StaffNodeRequest> staff;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public List<VisionaryNodeRequest> getVisionaries() {
        return visionaries;
    }

    public void setVisionaries(List<VisionaryNodeRequest> visionaries) {
        this.visionaries = visionaries;
    }

    public List<StaffNodeRequest> getStaff() {
        return staff;
    }

    public void setStaff(List<StaffNodeRequest> staff) {
        this.staff = staff;
    }

    public List<MasterLifeNodeRequest> getMasterLives() {
        return masterLives;
    }

    public void setMasterLives(List<MasterLifeNodeRequest> masterLives) {
        this.masterLives = masterLives;
    }
}


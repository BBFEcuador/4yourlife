package com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class VisionaryNodeRequest {
    private String id;
    private String userId;
    @Valid
    private List<StaffNodeRequest> staff;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<StaffNodeRequest> getStaff() {
        return staff;
    }

    public void setStaff(List<StaffNodeRequest> staff) {
        this.staff = staff;
    }
}

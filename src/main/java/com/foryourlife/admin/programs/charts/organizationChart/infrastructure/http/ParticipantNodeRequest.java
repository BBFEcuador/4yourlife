package com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http;

public class ParticipantNodeRequest {
    private String id;
    private String userId;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

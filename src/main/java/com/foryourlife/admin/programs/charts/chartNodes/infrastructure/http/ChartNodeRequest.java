package com.foryourlife.admin.programs.charts.chartNodes.infrastructure.http;

import com.foryourlife.shared.domain.level.CourseLevel;

public class ChartNodeRequest {
    private String userId;
    private String organizationId;
    private String level;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

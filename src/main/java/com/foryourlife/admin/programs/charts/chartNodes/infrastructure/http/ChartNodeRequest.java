package com.foryourlife.admin.programs.charts.chartNodes.infrastructure.http;

import jakarta.validation.constraints.NotNull;

public class ChartNodeRequest {
    @NotNull(message = "El usuario es requerido")
    private String userId;
    private String parentNodeId;
    private String parentId;
    private String level;
    @NotNull(message = "El usuario es requerido")
    private String organizationChartId;


    public String getUserId() {
        return userId;
    }

    public String getParentNodeId() {
        return parentNodeId;
    }

    public String getParentId() {
        return parentId;
    }

    public String getLevel() {
        return level;
    }

    public String getOrganizationChartId() {
        return organizationChartId;
    }
}

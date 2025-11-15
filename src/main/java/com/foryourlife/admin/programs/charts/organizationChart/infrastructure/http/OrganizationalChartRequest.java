package com.foryourlife.admin.programs.charts.organizationChart.infrastructure.http;

import com.foryourlife.admin.programs.charts.chartNodes.infrastructure.http.ChartNodeRequest;

import java.util.List;

public class OrganizationalChartRequest {
    public String teamId;
    public List<ChartNodeRequest> chartNodeRequests;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public List<ChartNodeRequest> getChartNodeRequests() {
        return chartNodeRequests;
    }

    public void setChartNodeRequests(List<ChartNodeRequest> chartNodeRequests) {
        this.chartNodeRequests = chartNodeRequests;
    }
}


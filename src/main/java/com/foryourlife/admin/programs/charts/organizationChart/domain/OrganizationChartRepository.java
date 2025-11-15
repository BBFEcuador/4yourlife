package com.foryourlife.admin.programs.charts.organizationChart.domain;

import java.util.List;
import java.util.Optional;

public interface OrganizationChartRepository {
    void save(OrganizationChart organizationChart);
    List<OrganizationChart> getOrganizationChartsByTeamId(String teamId);
    List<OrganizationChart> getAllOrganizationCharts();
    Optional<OrganizationChart> findById(String id);
}

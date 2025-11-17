package com.foryourlife.admin.programs.charts.organizationChart.domain;

import com.foryourlife.shared.domain.level.CourseLevel;

import java.util.List;
import java.util.Optional;

public interface OrganizationChartRepository {
    void save(OrganizationChart organizationChart);

    List<OrganizationChart> getOrganizationChartsByTeamId(String teamId);

    List<OrganizationChart> getAllOrganizationCharts();

    Optional<OrganizationChart> getOrganizationChartByTeamIdAndCourseLevel(String teamId, CourseLevel courseLevel);

    void deleteOrganizationChartById(String id);

    Optional<OrganizationChart> getOrganizationChartTrainingId(String trainingId);

    Optional<OrganizationChart> findById(String id);
}

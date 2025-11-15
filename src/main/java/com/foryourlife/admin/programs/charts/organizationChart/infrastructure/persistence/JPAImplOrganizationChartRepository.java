package com.foryourlife.admin.programs.charts.organizationChart.infrastructure.persistence;

import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPAImplOrganizationChartRepository extends JpaRepository<OrganizationChart, String> {
    List<OrganizationChart> findAllByTeam_Id(String teamId);
}

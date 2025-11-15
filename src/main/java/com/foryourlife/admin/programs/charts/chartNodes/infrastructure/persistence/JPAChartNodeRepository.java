package com.foryourlife.admin.programs.charts.chartNodes.infrastructure.persistence;

import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPAChartNodeRepository extends JpaRepository<ChartNode, String> {
    List<ChartNode> findAllByOrganizationChart_Id(String organizationChartId);
}

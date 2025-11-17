package com.foryourlife.admin.programs.charts.chartNodes.domain;

import com.foryourlife.admin.programs.charts.chartNodes.infrastructure.http.ChartNodeRequest;

import java.util.List;
import java.util.Optional;

public interface ChartNodeRepository {
    void save(ChartNode chartNode);
    void saveAll(Iterable<ChartNode> chartNodes);
    List<ChartNode> findAll();
    List<ChartNode> findAllByOrganizationId(String organizationId);
    void updateNode(ChartNode chartNode);
    Optional<ChartNode> findById(String chartNodeId);
}

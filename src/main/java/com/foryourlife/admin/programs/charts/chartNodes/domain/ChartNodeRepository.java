package com.foryourlife.admin.programs.charts.chartNodes.domain;

import java.util.List;

public interface ChartNodeRepository {
    void save(ChartNode chartNode);
    void saveAll(Iterable<ChartNode> chartNodes);
    List<ChartNode> findAll();
    List<ChartNode> findAllByOrganizationId(String organizationId);
}

package com.foryourlife.admin.programs.charts.chartNodes.infrastructure.persistence;

import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JPAImplChartNodeRepository implements ChartNodeRepository {
    private final JPAChartNodeRepository repository;

    public JPAImplChartNodeRepository(JPAChartNodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(ChartNode chartNode) {
        repository.save(chartNode);
    }

    @Override
    public void saveAll(Iterable<ChartNode> chartNodes) {
        repository.saveAll(chartNodes);
    }

    @Override
    public List<ChartNode> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ChartNode> findAllByOrganizationId(String organizationId) {
        return repository.findAllByOrganizationChart_Id(organizationId);
    }
}

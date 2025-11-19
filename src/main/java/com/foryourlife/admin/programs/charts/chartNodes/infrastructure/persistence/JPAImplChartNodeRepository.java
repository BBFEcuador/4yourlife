package com.foryourlife.admin.programs.charts.chartNodes.infrastructure.persistence;

import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public void updateNode(ChartNode chartNode) {
        repository.save(chartNode);
    }

    @Override
    public Optional<ChartNode> findById(String chartNodeId) {
        return repository.findById(chartNodeId);
    }

    @Override
    public void deleteNode(ChartNode chartNode) {
        repository.delete(chartNode);
    }

    @Override
    public List<ChartNode> findAllByParentNodeId(String parentNodeId) {
        return repository.findAllByParentNodeId(parentNodeId);
    }
}

package com.foryourlife.admin.programs.charts.chartNodes.application;

import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChartNodeQueryService {
    private final ChartNodeRepository repository;

    public ChartNodeQueryService(ChartNodeRepository repository) {
        this.repository = repository;
    }

    public List<ChartNode> findAllByOrganizationId(String organizationId) {
        return repository.findAllByOrganizationId(organizationId);
    }

    public List<ChartNode> findAll(){
        return repository.findAll();
    }
}

package com.foryourlife.admin.programs.charts.chartNodes.application;

import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNodeRepository;
import org.springframework.stereotype.Service;

@Service
public class ChartNodeCommandService {
    private final ChartNodeRepository repository;

    public ChartNodeCommandService(ChartNodeRepository repository) {
        this.repository = repository;
    }
}

package com.foryourlife.admin.programs.charts.organizationChart.application;

import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChartRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationChartQueryService {
    private final OrganizationChartRepository organizationChartRepository;

    public OrganizationChartQueryService(OrganizationChartRepository organizationChartRepository) {
        this.organizationChartRepository = organizationChartRepository;
    }

    public OrganizationChart getOrganizationChartById(String id){
        return organizationChartRepository.findById(id).orElseThrow(()-> new BaseException(
                "Chart not found",
                List.of()
        ));
    }

    public List<OrganizationChart> getAllOrganizationCharts(){
        return organizationChartRepository.getAllOrganizationCharts();
    }

    public List<OrganizationChart> getOrganizationChartByTeamId(String teamId){
        return organizationChartRepository.getOrganizationChartsByTeamId(teamId);
    }

    public Optional<OrganizationChart> getOrganizationChartByTrainingId(String trainingId){
        return organizationChartRepository.getOrganizationChartTrainingId(trainingId);
    }
}

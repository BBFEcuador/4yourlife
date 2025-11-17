package com.foryourlife.admin.programs.charts.organizationChart.infrastructure.persistence;

import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChartRepository;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPAOrganizationChartRepository implements OrganizationChartRepository {
    private final JPAImplOrganizationChartRepository jpaImplOrganizationChartRepository;

    public JPAOrganizationChartRepository(JPAImplOrganizationChartRepository jpaImplOrganizationChartRepository) {
        this.jpaImplOrganizationChartRepository = jpaImplOrganizationChartRepository;
    }

    @Override
    public void save(OrganizationChart organizationChart) {
        jpaImplOrganizationChartRepository.save(organizationChart);
    }

    @Override
    public List<OrganizationChart> getOrganizationChartsByTeamId(String teamId) {
        return jpaImplOrganizationChartRepository.findAllByTeam_Id(teamId);
    }

    @Override
    public List<OrganizationChart> getAllOrganizationCharts() {
        return jpaImplOrganizationChartRepository.findAll();
    }

    @Override
    public Optional<OrganizationChart> findById(String id) {
        return jpaImplOrganizationChartRepository.findById(id);

    }

    @Override
    public Optional<OrganizationChart> getOrganizationChartTrainingId(String trainingId) {
        return jpaImplOrganizationChartRepository.findByTeam_Training_Id(trainingId);
    }

    @Override
    public Optional<OrganizationChart> getOrganizationChartByTeamIdAndCourseLevel(String teamId, CourseLevel courseLevel) {
        return jpaImplOrganizationChartRepository.findByTeam_IdAndCourseLevel(teamId, courseLevel);
    }

    @Override
    public void deleteOrganizationChartById(String id) {

    }
}

package com.foryourlife.admin.programs.charts.organizationChart.infrastructure.persistence;

import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JPAImplOrganizationChartRepository extends JpaRepository<OrganizationChart, String> {
    List<OrganizationChart> findAllByTeam_Id(String teamId);

    Optional<OrganizationChart> findByTeam_IdAndCourseLevel(String teamId, CourseLevel courseLevel);

    Optional<OrganizationChart> findByTeam_Training_Id(String teamTrainingId);

    @Query("""
                SELECT oc FROM OrganizationChart oc
                LEFT JOIN FETCH oc.nodes n
                WHERE oc.team.training.id = :teamTrainingId AND oc.courseLevel = oc.team.training.courseLevel
            """)
    Optional<OrganizationChart> findRootNodesByTeamTrainingId(String teamTrainingId);
}

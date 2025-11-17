package com.foryourlife.admin.programs.charts.organizationChart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organization_chart")
public class OrganizationChart {
    @Id
    private String id;
    @OneToMany(
            mappedBy = "organizationChart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonIgnoreProperties({"organizationChart"})
    private List<ChartNode> nodes = new ArrayList<>();
    @ManyToOne
    @JoinColumn(
        name = "team_id",
        referencedColumnName = "id"
    )
    @JsonIgnore
    private Team team;
    @Column(
        name = "course_level",
        nullable = false
    )
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    public OrganizationChart(){

    }

    public OrganizationChart(String id, List<ChartNode> nodes, Team team, CourseLevel courseLevel) {
        this.id = id;
        this.nodes = nodes;
        this.team = team;
        this.courseLevel = courseLevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ChartNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ChartNode> nodes) {
        this.nodes = nodes;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }
}

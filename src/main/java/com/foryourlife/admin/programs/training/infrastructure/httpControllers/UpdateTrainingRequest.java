package com.foryourlife.admin.programs.training.infrastructure.httpControllers;

import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.domain.EndDate;
import com.foryourlife.admin.programs.training.domain.StartDate;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.shared.domain.level.CourseLevel;

public class UpdateTrainingRequest {
    private String id;
    private Integer number;
    private String name;
    private StartDate startDate;
    private EndDate endDate;
    private CourseLevel courseLevel;
    private Training nextLevel;
    private Campus campus;
    private Boolean state;
    private Team originalTeam;

    public UpdateTrainingRequest(String id, Integer number, String name, StartDate startDate, EndDate endDate, CourseLevel courseLevel, Training nextLevel, Campus campus, Boolean state, Team originalTeam) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseLevel = courseLevel;
        this.nextLevel = nextLevel;
        this.campus = campus;
        this.state = state;
        this.originalTeam = originalTeam;
    }

    public Training toDomain() {
        return Training.create(id, number, name, startDate.getValue(), endDate.getValue(), courseLevel, nextLevel, campus, state);
    }

    public String getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public StartDate getStartDate() {
        return startDate;
    }

    public EndDate getEndDate() {
        return endDate;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public Training getNextLevel() {
        return nextLevel;
    }

    public Campus getCampus() {
        return campus;
    }

    public Boolean getState() {
        return state;
    }

    public Team getOriginalTeam() {
        return originalTeam;
    }
}

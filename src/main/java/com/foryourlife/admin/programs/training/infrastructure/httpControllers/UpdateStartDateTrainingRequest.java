package com.foryourlife.admin.programs.training.infrastructure.httpControllers;

import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.domain.EndDate;
import com.foryourlife.admin.programs.training.domain.StartDate;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.shared.domain.level.CourseLevel;

import java.time.LocalDate;

public class UpdateStartDateTrainingRequest {
    private String id;
    private LocalDate startDate;


    public String getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}

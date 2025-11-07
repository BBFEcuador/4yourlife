package com.foryourlife.admin.programs.trainer.domain;

import java.util.List;

public class TrainingDashboardDto {
    private String name;
    private String trainerName;
    private AttendanceDashboard attendanceDashboard;

    public TrainingDashboardDto(String name, String trainerName, AttendanceDashboard attendanceDashboard) {
        this.name = name;
        this.trainerName = trainerName;
        this.attendanceDashboard = attendanceDashboard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public AttendanceDashboard getAttendanceDashboard() {
        return attendanceDashboard;
    }

    public void setAttendanceDashboard(AttendanceDashboard attendanceDashboard) {
        this.attendanceDashboard = attendanceDashboard;
    }
}

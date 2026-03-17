package com.foryourlife.admin.dashboard.trainerDashboard.domain.life;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.LingererStats;

import java.util.List;

public class TrainerLifeView {
    private String trainingName;
    private String trainerName;
    private AttendanceDashboard attendanceDashboard;
    private PromiseDashboard promiseDashboard;
    private List<UserDashboardDto> users;
    private LingererStats lingererStats;


    public TrainerLifeView(String trainingName, String trainerName, AttendanceDashboard attendanceDashboard, PromiseDashboard promiseDashboard, List<UserDashboardDto> users, LingererStats lingererStats) {
        this.trainingName = trainingName;
        this.trainerName = trainerName;
        this.attendanceDashboard = attendanceDashboard;
        this.promiseDashboard = promiseDashboard;
        this.users = users;
        this.lingererStats = lingererStats;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
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

    public PromiseDashboard getPromiseDashboard() {
        return promiseDashboard;
    }

    public void setPromiseDashboard(PromiseDashboard promiseDashboard) {
        this.promiseDashboard = promiseDashboard;
    }

    public List<UserDashboardDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDashboardDto> users) {
        this.users = users;
    }

    public LingererStats getLingererStats() {
        return lingererStats;
    }

    public void setLingererStats(LingererStats lingererStats) {
        this.lingererStats = lingererStats;
    }
}
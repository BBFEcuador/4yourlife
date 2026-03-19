package com.foryourlife.admin.dashboard.trainerDashboard.domain.life;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.LingererStats;
import com.foryourlife.shared.domain.level.CourseLevel;

import java.util.List;

public class TrainerLifeView {
    private String trainingName;
    private String trainerName;
    private String trainingDate;
    private String courseLevel;
    private LifeAttendanceDashboard lifeAttendanceDashboard;
    private PromiseDashboard promiseDashboard;
    private List<UserDashboardDto> users;
    private LingererStats lingererStats;

    public TrainerLifeView(String trainingName, String trainerName, String trainingDate, String courseLevel, LifeAttendanceDashboard lifeAttendanceDashboard, PromiseDashboard promiseDashboard, List<UserDashboardDto> users, LingererStats lingererStats) {
        this.trainingName = trainingName;
        this.trainerName = trainerName;
        this.trainingDate = trainingDate;
        this.courseLevel = courseLevel;
        this.lifeAttendanceDashboard = lifeAttendanceDashboard;
        this.promiseDashboard = promiseDashboard;
        this.users = users;
        this.lingererStats = lingererStats;
    }

    public String getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(String trainingDate) {
        this.trainingDate = trainingDate;
    }

    public LifeAttendanceDashboard getLifeAttendanceDashboard() {
        return lifeAttendanceDashboard;
    }

    public void setLifeAttendanceDashboard(LifeAttendanceDashboard lifeAttendanceDashboard) {
        this.lifeAttendanceDashboard = lifeAttendanceDashboard;
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

    public LifeAttendanceDashboard getAttendanceDashboard() {
        return lifeAttendanceDashboard;
    }

    public void setAttendanceDashboard(LifeAttendanceDashboard lifeAttendanceDashboard) {
        this.lifeAttendanceDashboard = lifeAttendanceDashboard;
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

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }
}
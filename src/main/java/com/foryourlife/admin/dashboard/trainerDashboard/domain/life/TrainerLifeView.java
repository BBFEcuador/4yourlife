package com.foryourlife.admin.dashboard.trainerDashboard.domain.life;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.LingererStats;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.PreviousTrainingStats;
import com.foryourlife.shared.domain.level.CourseLevel;

import java.util.List;

public class TrainerLifeView {
    private String trainingName;
    private String trainerName;
    private String trainingDate;
    private String courseLevel;

    private PreviousTrainingStats previousTrainingStats;
    private List<UserDashboardDto> users;
    private DeclarationStats  declarationStats;
    private LifeAttendanceDashboard lifeAttendanceDashboard;
    private LingererStats lingererStats;

    public TrainerLifeView(String trainingName, String trainerName, String trainingDate, String courseLevel, PreviousTrainingStats previousTrainingStats, List<UserDashboardDto> users, DeclarationStats declarationStats, LifeAttendanceDashboard lifeAttendanceDashboard, LingererStats lingererStats) {
        this.trainingName = trainingName;
        this.trainerName = trainerName;
        this.trainingDate = trainingDate;
        this.courseLevel = courseLevel;
        this.previousTrainingStats = previousTrainingStats;
        this.users = users;
        this.declarationStats = declarationStats;
        this.lifeAttendanceDashboard = lifeAttendanceDashboard;
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

    public String getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(String trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public LifeAttendanceDashboard getLifeAttendanceDashboard() {
        return lifeAttendanceDashboard;
    }

    public void setLifeAttendanceDashboard(LifeAttendanceDashboard lifeAttendanceDashboard) {
        this.lifeAttendanceDashboard = lifeAttendanceDashboard;
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

    public PreviousTrainingStats getPreviousTrainingStats() {
        return previousTrainingStats;
    }

    public void setPreviousTrainingStats(PreviousTrainingStats previousTrainingStats) {
        this.previousTrainingStats = previousTrainingStats;
    }

    public DeclarationStats getDeclarationStats() {
        return declarationStats;
    }

    public void setDeclarationStats(DeclarationStats declarationStats) {
        this.declarationStats = declarationStats;
    }
}
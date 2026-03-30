package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life;

import java.util.List;

public class OperativeLifeDashboard {
    private String trainingName;
    private String trainerName;
    private String courseLevel;
    private WeekendLifeReport weekendLifeReport;
    private List<CallTypeStats> callsInfoList;

    public OperativeLifeDashboard(String trainingName, String trainerName, String courseLevel, WeekendLifeReport weekendLifeReport, List<CallTypeStats> callsInfoList) {
        this.trainingName = trainingName;
        this.trainerName = trainerName;
        this.courseLevel = courseLevel;
        this.weekendLifeReport = weekendLifeReport;
        this.callsInfoList = callsInfoList;
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

    public WeekendLifeReport getWeekendReport() {
        return weekendLifeReport;
    }

    public void setWeekendReport(WeekendLifeReport weekendLifeReport) {
        this.weekendLifeReport = weekendLifeReport;
    }

    public List<CallTypeStats> getCallsInfoList() {
        return callsInfoList;
    }

    public void setCallsInfoList(List<CallTypeStats> callsInfoList) {
        this.callsInfoList = callsInfoList;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }
}

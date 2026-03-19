package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.common.CallTypeStats;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.WeeklyPaymentStats;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.common.WeekendReport;

import java.util.List;

public class OperativeLifeDashboard {
    private String trainingName;
    private String trainerName;
    private WeekendReport weekendReport;
    private List<CallTypeStats> callsInfoList;

    public OperativeLifeDashboard(String trainingName, String trainerName, WeekendReport weekendReport, List<CallTypeStats> callsInfoList) {
        this.trainingName = trainingName;
        this.trainerName = trainerName;
        this.weekendReport = weekendReport;
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

    public WeekendReport getWeekendReport() {
        return weekendReport;
    }

    public void setWeekendReport(WeekendReport weekendReport) {
        this.weekendReport = weekendReport;
    }

    public List<CallTypeStats> getCallsInfoList() {
        return callsInfoList;
    }

    public void setCallsInfoList(List<CallTypeStats> callsInfoList) {
        this.callsInfoList = callsInfoList;
    }

}

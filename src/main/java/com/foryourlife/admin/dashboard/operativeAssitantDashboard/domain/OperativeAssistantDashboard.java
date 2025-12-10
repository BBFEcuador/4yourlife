package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain;

import java.util.List;

public class OperativeAssistantDashboard{
    List<TrainingInfo> trainingInfo;

    public OperativeAssistantDashboard(List<TrainingInfo> trainingInfo) {
        this.trainingInfo = trainingInfo;
    }

    public List<TrainingInfo> getTrainingInfo() {
        return trainingInfo;
    }

    public void setTrainingInfo(List<TrainingInfo> trainingInfo) {
        this.trainingInfo = trainingInfo;
    }
}

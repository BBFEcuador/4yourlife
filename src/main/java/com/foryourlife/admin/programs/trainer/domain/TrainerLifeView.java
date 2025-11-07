package com.foryourlife.admin.programs.trainer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.training.domain.Training;

import java.util.List;

public class TrainerLifeView {
    private List<TrainingDashboardDto> trainingInfo;

    public TrainerLifeView(List<TrainingDashboardDto> trainingInfo) {
        this.trainingInfo = trainingInfo;
    }

    public List<TrainingDashboardDto> getTrainingInfo() {
        return trainingInfo;
    }

    public void setTrainingInfo(List<TrainingDashboardDto> trainingInfo) {
        this.trainingInfo = trainingInfo;
    }
}

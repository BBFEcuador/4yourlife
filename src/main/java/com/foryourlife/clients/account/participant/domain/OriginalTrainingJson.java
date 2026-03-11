package com.foryourlife.clients.account.participant.domain;

public class OriginalTrainingJson {
    private String trainingName;
    private String trainingId;

    public OriginalTrainingJson(String trainingName, String trainingId) {
        this.trainingName = trainingName;
        this.trainingId = trainingId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }
}

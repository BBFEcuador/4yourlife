package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.GeneralAttendance;

public class OperativeYourDashboard {
    private String trainingName;
    private String trainerName;
    private GeneralAttendance attendance;
    private OperativeYourPayments operativeYourPayments;

    public OperativeYourDashboard(String trainingName, String trainerName, GeneralAttendance attendance, OperativeYourPayments operativeYourPayments) {
        this.trainingName = trainingName;
        this.trainerName = trainerName;
        this.attendance = attendance;
        this.operativeYourPayments = operativeYourPayments;
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

    public GeneralAttendance getAttendance() {
        return attendance;
    }

    public void setAttendance(GeneralAttendance attendance) {
        this.attendance = attendance;
    }

    public OperativeYourPayments getOperativeYourPayments() {
        return operativeYourPayments;
    }

    public void setOperativeYourPayments(OperativeYourPayments operativeYourPayments) {
        this.operativeYourPayments = operativeYourPayments;
    }
}

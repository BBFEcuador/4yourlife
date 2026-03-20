package com.foryourlife.admin.dashboard.trainerDashboard.domain.common;

public class PreviousTrainingStats {
    private int attendeesFromPreviousTraining;
    private int finalPaymentsFromPreviousTraining;
    private int previousLifePayments;
    private double attendeesFromPreviousTrainingPercentage;
    private double finalPaymentsFromPreviousTrainingPercentage;

    public PreviousTrainingStats(int attendeesFromPreviousTraining, int finalPaymentsFromPreviousTraining, int previousLifePayments, double attendeesFromPreviousTrainingPercentage, double finalPaymentsFromPreviousTrainingPercentage) {
        this.attendeesFromPreviousTraining = attendeesFromPreviousTraining;
        this.finalPaymentsFromPreviousTraining = finalPaymentsFromPreviousTraining;
        this.previousLifePayments = previousLifePayments;
        this.attendeesFromPreviousTrainingPercentage = attendeesFromPreviousTrainingPercentage;
        this.finalPaymentsFromPreviousTrainingPercentage = finalPaymentsFromPreviousTrainingPercentage;
    }

    public int getPreviousLifePayments() {
        return previousLifePayments;
    }

    public void setPreviousLifePayments(int previousLifePayments) {
        this.previousLifePayments = previousLifePayments;
    }

    public int getAttendeesFromPreviousTraining() {
        return attendeesFromPreviousTraining;
    }

    public void setAttendeesFromPreviousTraining(int attendeesFromPreviousTraining) {
        this.attendeesFromPreviousTraining = attendeesFromPreviousTraining;
    }

    public int getFinalPaymentsFromPreviousTraining() {
        return finalPaymentsFromPreviousTraining;
    }

    public void setFinalPaymentsFromPreviousTraining(int finalPaymentsFromPreviousTraining) {
        this.finalPaymentsFromPreviousTraining = finalPaymentsFromPreviousTraining;
    }

    public double getAttendeesFromPreviousTrainingPercentage() {
        return attendeesFromPreviousTrainingPercentage;
    }

    public void setAttendeesFromPreviousTrainingPercentage(double attendeesFromPreviousTrainingPercentage) {
        this.attendeesFromPreviousTrainingPercentage = attendeesFromPreviousTrainingPercentage;
    }

    public double getFinalPaymentsFromPreviousTrainingPercentage() {
        return finalPaymentsFromPreviousTrainingPercentage;
    }

    public void setFinalPaymentsFromPreviousTrainingPercentage(double finalPaymentsFromPreviousTrainingPercentage) {
        this.finalPaymentsFromPreviousTrainingPercentage = finalPaymentsFromPreviousTrainingPercentage;
    }
}

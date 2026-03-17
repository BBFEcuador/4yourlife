package com.foryourlife.admin.dashboard.trainerDashboard.domain.common;

public class PreviousTrainingStats {
    private int attendeesFromPreviousTraining;
    private int previousLifePayments;

    public PreviousTrainingStats(int attendeesFromPreviousTraining, int previousLifePayments) {
        this.attendeesFromPreviousTraining = attendeesFromPreviousTraining;
        this.previousLifePayments = previousLifePayments;
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
}

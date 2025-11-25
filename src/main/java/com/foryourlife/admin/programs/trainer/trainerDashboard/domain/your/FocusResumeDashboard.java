package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.your;

public class FocusResumeDashboard {
    private int totalFocusSessions;
    private int yourPayment;
    private int lifePayment;
    private int totalPayment;

    public FocusResumeDashboard(int totalFocusSessions, int yourPayment, int lifePayment, int totalPayment) {
        this.totalFocusSessions = totalFocusSessions;
        this.yourPayment = yourPayment;
        this.lifePayment = lifePayment;
        this.totalPayment = totalPayment;
    }

    public int getTotalFocusSessions() {
        return totalFocusSessions;
    }

    public void setTotalFocusSessions(int totalFocusSessions) {
        this.totalFocusSessions = totalFocusSessions;
    }

    public int getYourPayment() {
        return yourPayment;
    }

    public void setYourPayment(int yourPayment) {
        this.yourPayment = yourPayment;
    }

    public int getLifePayment() {
        return lifePayment;
    }

    public void setLifePayment(int lifePayment) {
        this.lifePayment = lifePayment;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }
}

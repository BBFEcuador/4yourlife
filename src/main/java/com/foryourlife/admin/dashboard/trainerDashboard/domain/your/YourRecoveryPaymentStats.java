package com.foryourlife.admin.dashboard.trainerDashboard.domain.your;

public class YourRecoveryPaymentStats {
    private int recovered;
    private int recoveredWithPreviousLifePayment;
    private double percentage;

    public YourRecoveryPaymentStats(int recovered, int recoveredWithPreviousLifePayment, double percentage) {
        this.recovered = recovered;
        this.recoveredWithPreviousLifePayment = recoveredWithPreviousLifePayment;
        this.percentage = percentage;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getRecoveredWithPreviousLifePayment() {
        return recoveredWithPreviousLifePayment;
    }

    public void setRecoveredWithPreviousLifePayment(int recoveredWithPreviousLifePayment) {
        this.recoveredWithPreviousLifePayment = recoveredWithPreviousLifePayment;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}

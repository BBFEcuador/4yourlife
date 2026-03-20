package com.foryourlife.admin.dashboard.trainerDashboard.domain.your;

public class YourRecoveryPaymentStats {
    private int recoveredWithPreviousLifePayment;
    private double percentage;

    public YourRecoveryPaymentStats(int recoveredWithPreviousLifePayment, double percentage) {
        this.recoveredWithPreviousLifePayment = recoveredWithPreviousLifePayment;
        this.percentage = percentage;
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

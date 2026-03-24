package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your;

public class OperativeYourPayments {
    private int previousPayments;
    private int saturdayPayments;
    private int sundayPayments;
    private int totalPayments;
    private double totalPaymentsPercentage;

    public OperativeYourPayments(int previousPayments, int saturdayPayments, int sundayPayments, int totalPayments, double totalPaymentsPercentage) {
        this.previousPayments = previousPayments;
        this.saturdayPayments = saturdayPayments;
        this.sundayPayments = sundayPayments;
        this.totalPayments = totalPayments;
        this.totalPaymentsPercentage = totalPaymentsPercentage;
    }

    public int getPreviousPayments() {
        return previousPayments;
    }

    public void setPreviousPayments(int previousPayments) {
        this.previousPayments = previousPayments;
    }

    public int getSaturdayPayments() {
        return saturdayPayments;
    }

    public void setSaturdayPayments(int saturdayPayments) {
        this.saturdayPayments = saturdayPayments;
    }

    public int getSundayPayments() {
        return sundayPayments;
    }

    public void setSundayPayments(int sundayPayments) {
        this.sundayPayments = sundayPayments;
    }

    public int getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(int totalPayments) {
        this.totalPayments = totalPayments;
    }

    public double getTotalPaymentsPercentage() {
        return totalPaymentsPercentage;
    }

    public void setTotalPaymentsPercentage(double totalPaymentsPercentage) {
        this.totalPaymentsPercentage = totalPaymentsPercentage;
    }
}

package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your;

import java.util.List;

public class OperativeYourPayments {
    private int previousPayments;
    private double previousPaymentsPercentage;
    private int saturdayPayments;
    private double saturdayPaymentsPercentage;
    private int sundayPayments;
    private double sundayPaymentsPercentage;
    private int totalPayments;
    private double totalPaymentsPercentage;
    private List<YourWeeklyPaymentStats> weeklyPaymentStats;

    public OperativeYourPayments(int previousPayments, double previousPaymentsPercentage, int saturdayPayments, double saturdayPaymentsPercentage, int sundayPayments, double sundayPaymentsPercentage, int totalPayments, double totalPaymentsPercentage, List<YourWeeklyPaymentStats> weeklyPaymentStats) {
        this.previousPayments = previousPayments;
        this.previousPaymentsPercentage = previousPaymentsPercentage;
        this.saturdayPayments = saturdayPayments;
        this.saturdayPaymentsPercentage = saturdayPaymentsPercentage;
        this.sundayPayments = sundayPayments;
        this.sundayPaymentsPercentage = sundayPaymentsPercentage;
        this.totalPayments = totalPayments;
        this.totalPaymentsPercentage = totalPaymentsPercentage;
        this.weeklyPaymentStats = weeklyPaymentStats;
    }

    public double getPreviousPaymentsPercentage() {
        return previousPaymentsPercentage;
    }

    public void setPreviousPaymentsPercentage(double previousPaymentsPercentage) {
        this.previousPaymentsPercentage = previousPaymentsPercentage;
    }

    public double getSaturdayPaymentsPercentage() {
        return saturdayPaymentsPercentage;
    }

    public void setSaturdayPaymentsPercentage(double saturdayPaymentsPercentage) {
        this.saturdayPaymentsPercentage = saturdayPaymentsPercentage;
    }

    public double getSundayPaymentsPercentage() {
        return sundayPaymentsPercentage;
    }

    public void setSundayPaymentsPercentage(double sundayPaymentsPercentage) {
        this.sundayPaymentsPercentage = sundayPaymentsPercentage;
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

    public List<YourWeeklyPaymentStats> getWeeklyPaymentStats() {
        return weeklyPaymentStats;
    }

    public void setWeeklyPaymentStats(List<YourWeeklyPaymentStats> weeklyPaymentStats) {
        this.weeklyPaymentStats = weeklyPaymentStats;
    }
}

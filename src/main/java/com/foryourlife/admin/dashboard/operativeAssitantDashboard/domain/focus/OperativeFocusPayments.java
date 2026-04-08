package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.focus;

import java.util.List;

public class OperativeFocusPayments {
    private int yourPaymentsCount;
    private int yourPlusLifePaymentsCount;
    private int totalPaymentsCount;
    private double totalPaymentsPercentage;
    private int pendingPaymentsCount;
    private int possibilityPaymentsCount;
    private List<FocusWeeklyPaymentStats> focusWeeklyPaymentStats;

    public OperativeFocusPayments(int yourPaymentsCount, int yourPlusLifePaymentsCount, int totalPaymentsCount, double totalPaymentsPercentage, int pendingPaymentsCount, int possibilityPaymentsCount, List<FocusWeeklyPaymentStats> focusWeeklyPaymentStats) {
        this.yourPaymentsCount = yourPaymentsCount;
        this.yourPlusLifePaymentsCount = yourPlusLifePaymentsCount;
        this.totalPaymentsCount = totalPaymentsCount;
        this.totalPaymentsPercentage = totalPaymentsPercentage;
        this.pendingPaymentsCount = pendingPaymentsCount;
        this.possibilityPaymentsCount = possibilityPaymentsCount;
        this.focusWeeklyPaymentStats = focusWeeklyPaymentStats;
    }

    public int getYourPaymentsCount() {
        return yourPaymentsCount;
    }

    public void setYourPaymentsCount(int yourPaymentsCount) {
        this.yourPaymentsCount = yourPaymentsCount;
    }

    public int getYourPlusLifePaymentsCount() {
        return yourPlusLifePaymentsCount;
    }

    public void setYourPlusLifePaymentsCount(int yourPlusLifePaymentsCount) {
        this.yourPlusLifePaymentsCount = yourPlusLifePaymentsCount;
    }

    public int getTotalPaymentsCount() {
        return totalPaymentsCount;
    }

    public void setTotalPaymentsCount(int totalPaymentsCount) {
        this.totalPaymentsCount = totalPaymentsCount;
    }

    public int getPendingPaymentsCount() {
        return pendingPaymentsCount;
    }

    public void setPendingPaymentsCount(int pendingPaymentsCount) {
        this.pendingPaymentsCount = pendingPaymentsCount;
    }

    public int getPossibilityPaymentsCount() {
        return possibilityPaymentsCount;
    }

    public void setPossibilityPaymentsCount(int possibilityPaymentsCount) {
        this.possibilityPaymentsCount = possibilityPaymentsCount;
    }

    public List<FocusWeeklyPaymentStats> getFocusWeeklyPaymentStats() {
        return focusWeeklyPaymentStats;
    }

    public void setFocusWeeklyPaymentStats(List<FocusWeeklyPaymentStats> focusWeeklyPaymentStats) {
        this.focusWeeklyPaymentStats = focusWeeklyPaymentStats;
    }

    public double getTotalPaymentsPercentage() {
        return totalPaymentsPercentage;
    }

    public void setTotalPaymentsPercentage(double totalPaymentsPercentage) {
        this.totalPaymentsPercentage = totalPaymentsPercentage;
    }
}

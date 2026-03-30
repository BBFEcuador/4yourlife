package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.focus;

public class FocusDailyStats {
    private int yourPaymentsCount;
    private int yourPlusLifePaymentsCount;
    private int totalPaymentsCount;
    private int pendingPaymentsCount;
    private int agreedPaymentsCount;
    private int possiblePaymentsCount;
    private int notInterestPaymentsCount;
    private double passPercentage;
    private double projectedPercentage;

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

    public int getAgreedPaymentsCount() {
        return agreedPaymentsCount;
    }

    public void setAgreedPaymentsCount(int agreedPaymentsCount) {
        this.agreedPaymentsCount = agreedPaymentsCount;
    }

    public int getPossiblePaymentsCount() {
        return possiblePaymentsCount;
    }

    public void setPossiblePaymentsCount(int possiblePaymentsCount) {
        this.possiblePaymentsCount = possiblePaymentsCount;
    }

    public int getNotInterestPaymentsCount() {
        return notInterestPaymentsCount;
    }

    public void setNotInterestPaymentsCount(int notInterestPaymentsCount) {
        this.notInterestPaymentsCount = notInterestPaymentsCount;
    }

    public double getPassPercentage() {
        return passPercentage;
    }

    public void setPassPercentage(double passPercentage) {
        this.passPercentage = passPercentage;
    }

    public double getProjectedPercentage() {
        return projectedPercentage;
    }

    public void setProjectedPercentage(double projectedPercentage) {
        this.projectedPercentage = projectedPercentage;
    }
}

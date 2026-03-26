package com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your;

public class YourDailyStats {
    private int finalPaymentsCount;
    private int agreedPaymentsCount;
    private int totalPaymentsCount;
    private double passPaymentsPercentage;
    private double projectedPaymentsPercentage;

    public YourDailyStats(int finalPaymentsCount, int agreedPaymentsCount, int totalPaymentsCount, double passPaymentsPercentage, double projectedPaymentsPercentage) {
        this.finalPaymentsCount = finalPaymentsCount;
        this.agreedPaymentsCount = agreedPaymentsCount;
        this.totalPaymentsCount = totalPaymentsCount;
        this.passPaymentsPercentage = passPaymentsPercentage;
        this.projectedPaymentsPercentage = projectedPaymentsPercentage;
    }

    public int getFinalPaymentsCount() {
        return finalPaymentsCount;
    }

    public void setFinalPaymentsCount(int finalPaymentsCount) {
        this.finalPaymentsCount = finalPaymentsCount;
    }

    public int getAgreedPaymentsCount() {
        return agreedPaymentsCount;
    }

    public void setAgreedPaymentsCount(int agreedPaymentsCount) {
        this.agreedPaymentsCount = agreedPaymentsCount;
    }

    public int getTotalPaymentsCount() {
        return totalPaymentsCount;
    }

    public void setTotalPaymentsCount(int totalPaymentsCount) {
        this.totalPaymentsCount = totalPaymentsCount;
    }

    public double getPassPaymentsPercentage() {
        return passPaymentsPercentage;
    }

    public void setPassPaymentsPercentage(double passPaymentsPercentage) {
        this.passPaymentsPercentage = passPaymentsPercentage;
    }

    public double getProjectedPaymentsPercentage() {
        return projectedPaymentsPercentage;
    }

    public void setProjectedPaymentsPercentage(double projectedPaymentsPercentage) {
        this.projectedPaymentsPercentage = projectedPaymentsPercentage;
    }
}

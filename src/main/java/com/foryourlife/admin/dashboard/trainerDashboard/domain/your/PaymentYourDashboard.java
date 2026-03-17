package com.foryourlife.admin.dashboard.trainerDashboard.domain.your;

public class PaymentYourDashboard {
    public String staffName;

    public int previousLifePayments;
    public double previousPaymentsPercentage;

    public int saturdayPayments;
    public int accumulatedSaturdayPayments;
    public double passPercentageSaturday;

    public int sundayPayments;
    public int accumulatedSundayPayments;
    public double passPercentageSunday;

    public PaymentYourDashboard(String staffName, int previousLifePayments, double previousPaymentsPercentage, int saturdayPayments, int accumulatedSaturdayPayments, double passPercentageSaturday, int sundayPayments, int accumulatedSundayPayments, double passPercentageSunday) {
        this.staffName = staffName;
        this.previousLifePayments = previousLifePayments;
        this.previousPaymentsPercentage = previousPaymentsPercentage;
        this.saturdayPayments = saturdayPayments;
        this.accumulatedSaturdayPayments = accumulatedSaturdayPayments;
        this.passPercentageSaturday = passPercentageSaturday;
        this.sundayPayments = sundayPayments;
        this.accumulatedSundayPayments = accumulatedSundayPayments;
        this.passPercentageSunday = passPercentageSunday;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public int getPreviousLifePayments() {
        return previousLifePayments;
    }

    public void setPreviousLifePayments(int previousLifePayments) {
        this.previousLifePayments = previousLifePayments;
    }

    public double getPreviousPaymentsPercentage() {
        return previousPaymentsPercentage;
    }

    public void setPreviousPaymentsPercentage(double previousPaymentsPercentage) {
        this.previousPaymentsPercentage = previousPaymentsPercentage;
    }

    public int getSaturdayPayments() {
        return saturdayPayments;
    }

    public void setSaturdayPayments(int saturdayPayments) {
        this.saturdayPayments = saturdayPayments;
    }

    public int getAccumulatedSaturdayPayments() {
        return accumulatedSaturdayPayments;
    }

    public void setAccumulatedSaturdayPayments(int accumulatedSaturdayPayments) {
        this.accumulatedSaturdayPayments = accumulatedSaturdayPayments;
    }

    public double getPassPercentageSaturday() {
        return passPercentageSaturday;
    }

    public void setPassPercentageSaturday(double passPercentageSaturday) {
        this.passPercentageSaturday = passPercentageSaturday;
    }

    public int getSundayPayments() {
        return sundayPayments;
    }

    public void setSundayPayments(int sundayPayments) {
        this.sundayPayments = sundayPayments;
    }

    public int getAccumulatedSundayPayments() {
        return accumulatedSundayPayments;
    }

    public void setAccumulatedSundayPayments(int accumulatedSundayPayments) {
        this.accumulatedSundayPayments = accumulatedSundayPayments;
    }

    public double getPassPercentageSunday() {
        return passPercentageSunday;
    }

    public void setPassPercentageSunday(double passPercentageSunday) {
        this.passPercentageSunday = passPercentageSunday;
    }
}
package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

public class PaymentDashboard {
    public String staffName;

    public int yourPaymentsSunday;
    public int yourPlusLifePaymentsSunday;
    public int totalPaymentsSunday;
    public double passPercentageFinalSunday;

    public int yourPaymentsFinal;
    public int yourPlusLifePaymentsFinal;
    public int totalPaymentsFinal;
    public double passPercentageFinal;

    public PaymentDashboard(String staffName, int yourPaymentsSunday, int yourPlusLifePaymentsSunday, int totalPaymentsSunday, double passPercentageFinalSunday, int yourPaymentsFinal, int yourPlusLifePaymentsFinal, int totalPaymentsFinal, double passPercentageFinal) {
        this.staffName = staffName;
        this.yourPaymentsSunday = yourPaymentsSunday;
        this.yourPlusLifePaymentsSunday = yourPlusLifePaymentsSunday;
        this.totalPaymentsSunday = totalPaymentsSunday;
        this.passPercentageFinalSunday = passPercentageFinalSunday;
        this.yourPaymentsFinal = yourPaymentsFinal;
        this.yourPlusLifePaymentsFinal = yourPlusLifePaymentsFinal;
        this.totalPaymentsFinal = totalPaymentsFinal;
        this.passPercentageFinal = passPercentageFinal;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public int getYourPaymentsSunday() {
        return yourPaymentsSunday;
    }

    public void setYourPaymentsSunday(int yourPaymentsSunday) {
        this.yourPaymentsSunday = yourPaymentsSunday;
    }

    public int getYourPlusLifePaymentsSunday() {
        return yourPlusLifePaymentsSunday;
    }

    public void setYourPlusLifePaymentsSunday(int yourPlusLifePaymentsSunday) {
        this.yourPlusLifePaymentsSunday = yourPlusLifePaymentsSunday;
    }

    public int getTotalPaymentsSunday() {
        return totalPaymentsSunday;
    }

    public void setTotalPaymentsSunday(int totalPaymentsSunday) {
        this.totalPaymentsSunday = totalPaymentsSunday;
    }

    public double getPassPercentageFinalSunday() {
        return passPercentageFinalSunday;
    }

    public void setPassPercentageFinalSunday(double passPercentageFinalSunday) {
        this.passPercentageFinalSunday = passPercentageFinalSunday;
    }

    public int getYourPaymentsFinal() {
        return yourPaymentsFinal;
    }

    public void setYourPaymentsFinal(int yourPaymentsFinal) {
        this.yourPaymentsFinal = yourPaymentsFinal;
    }

    public int getYourPlusLifePaymentsFinal() {
        return yourPlusLifePaymentsFinal;
    }

    public void setYourPlusLifePaymentsFinal(int yourPlusLifePaymentsFinal) {
        this.yourPlusLifePaymentsFinal = yourPlusLifePaymentsFinal;
    }

    public int getTotalPaymentsFinal() {
        return totalPaymentsFinal;
    }

    public void setTotalPaymentsFinal(int totalPaymentsFinal) {
        this.totalPaymentsFinal = totalPaymentsFinal;
    }

    public double getPassPercentageFinal() {
        return passPercentageFinal;
    }

    public void setPassPercentageFinal(double passPercentageFinal) {
        this.passPercentageFinal = passPercentageFinal;
    }
}

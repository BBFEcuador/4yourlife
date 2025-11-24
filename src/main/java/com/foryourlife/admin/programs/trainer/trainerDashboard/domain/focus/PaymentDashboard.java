package com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus;

public class PaymentDashboard {
    public String staffName;
    public int completedPaymentCount;
    public int partialPaymentCount;
    public int totalPayments;
    public int yourPartialPaymentsCount;
    public int yourCompletedPaymentsCount;
    public int lifePartialPaymentsCount;
    public int lifeCompletedPaymentsCount;

    public PaymentDashboard(String staffName, int completedPaymentCount, int partialPaymentCount, int totalPayments, int yourPartialPaymentsCount, int yourCompletedPaymentsCount, int lifePartialPaymentsCount, int lifeCompletedPaymentsCount) {
        this.staffName = staffName;
        this.completedPaymentCount = completedPaymentCount;
        this.partialPaymentCount = partialPaymentCount;
        this.totalPayments = totalPayments;
        this.yourPartialPaymentsCount = yourPartialPaymentsCount;
        this.yourCompletedPaymentsCount = yourCompletedPaymentsCount;
        this.lifePartialPaymentsCount = lifePartialPaymentsCount;
        this.lifeCompletedPaymentsCount = lifeCompletedPaymentsCount;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public int getCompletedPaymentCount() {
        return completedPaymentCount;
    }

    public void setCompletedPaymentCount(int completedPaymentCount) {
        this.completedPaymentCount = completedPaymentCount;
    }

    public int getPartialPaymentCount() {
        return partialPaymentCount;
    }

    public void setPartialPaymentCount(int partialPaymentCount) {
        this.partialPaymentCount = partialPaymentCount;
    }

    public int getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(int totalPayments) {
        this.totalPayments = totalPayments;
    }

    public int getYourPartialPaymentsCount() {
        return yourPartialPaymentsCount;
    }

    public void setYourPartialPaymentsCount(int yourPartialPaymentsCount) {
        this.yourPartialPaymentsCount = yourPartialPaymentsCount;
    }

    public int getYourCompletedPaymentsCount() {
        return yourCompletedPaymentsCount;
    }

    public void setYourCompletedPaymentsCount(int yourCompletedPaymentsCount) {
        this.yourCompletedPaymentsCount = yourCompletedPaymentsCount;
    }

    public int getLifePartialPaymentsCount() {
        return lifePartialPaymentsCount;
    }

    public void setLifePartialPaymentsCount(int lifePartialPaymentsCount) {
        this.lifePartialPaymentsCount = lifePartialPaymentsCount;
    }

    public int getLifeCompletedPaymentsCount() {
        return lifeCompletedPaymentsCount;
    }

    public void setLifeCompletedPaymentsCount(int lifeCompletedPaymentsCount) {
        this.lifeCompletedPaymentsCount = lifeCompletedPaymentsCount;
    }
}

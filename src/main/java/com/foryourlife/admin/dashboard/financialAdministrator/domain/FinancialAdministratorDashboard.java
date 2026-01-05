package com.foryourlife.admin.dashboard.financialAdministrator.domain;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.WeeklyPaymentStats;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.PaymentMethodSummary;

import java.math.BigDecimal;
import java.util.List;

public class FinancialAdministratorDashboard{
    BigDecimal trainingTotalIncome;
    List<PaymentMethodSummary> paymentMethodSummary;
    List<WeeklyPaymentStats> weeklyPaymentStats;
    BigDecimal pendingPaymentsAmount;
    int totalPendingPayments;
    BigDecimal completedPaymentsAmount;
    int totalCompletedPayments;

    public FinancialAdministratorDashboard(BigDecimal trainingTotalIncome, List<PaymentMethodSummary> paymentMethodSummary, List<WeeklyPaymentStats> weeklyPaymentStats, BigDecimal pendingPaymentsAmount, int totalPendingPayments, BigDecimal completedPaymentsAmount, int totalCompletedPayments) {
        this.trainingTotalIncome = trainingTotalIncome;
        this.paymentMethodSummary = paymentMethodSummary;
        this.weeklyPaymentStats = weeklyPaymentStats;
        this.pendingPaymentsAmount = pendingPaymentsAmount;
        this.totalPendingPayments = totalPendingPayments;
        this.completedPaymentsAmount = completedPaymentsAmount;
        this.totalCompletedPayments = totalCompletedPayments;
    }

    public BigDecimal getTrainingTotalIncome() {
        return trainingTotalIncome;
    }

    public void setTrainingTotalIncome(BigDecimal trainingTotalIncome) {
        this.trainingTotalIncome = trainingTotalIncome;
    }

    public List<PaymentMethodSummary> getPaymentMethodSummary() {
        return paymentMethodSummary;
    }

    public void setPaymentMethodSummary(List<PaymentMethodSummary> paymentMethodSummary) {
        this.paymentMethodSummary = paymentMethodSummary;
    }

    public List<WeeklyPaymentStats> getWeeklyPaymentStats() {
        return weeklyPaymentStats;
    }

    public void setWeeklyPaymentStats(List<WeeklyPaymentStats> weeklyPaymentStats) {
        this.weeklyPaymentStats = weeklyPaymentStats;
    }

    public BigDecimal getPendingPaymentsAmount() {
        return pendingPaymentsAmount;
    }

    public void setPendingPaymentsAmount(BigDecimal pendingPaymentsAmount) {
        this.pendingPaymentsAmount = pendingPaymentsAmount;
    }

    public int getTotalPendingPayments() {
        return totalPendingPayments;
    }

    public void setTotalPendingPayments(int totalPendingPayments) {
        this.totalPendingPayments = totalPendingPayments;
    }

    public BigDecimal getCompletedPaymentsAmount() {
        return completedPaymentsAmount;
    }

    public void setCompletedPaymentsAmount(BigDecimal completedPaymentsAmount) {
        this.completedPaymentsAmount = completedPaymentsAmount;
    }

    public int getTotalCompletedPayments() {
        return totalCompletedPayments;
    }

    public void setTotalCompletedPayments(int totalCompletedPayments) {
        this.totalCompletedPayments = totalCompletedPayments;
    }
}

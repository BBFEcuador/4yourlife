package com.foryourlife.admin.dashboard.financialAdministrator.domain;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.PaymentMethodSummary;

import java.math.BigDecimal;
import java.util.List;

public class FinancialAdministratorDashboard{
    BigDecimal trainingTotalIncome;
    List<PaymentMethodSummary> paymentMethodSummary;

    public FinancialAdministratorDashboard(BigDecimal trainingTotalIncome, List<PaymentMethodSummary> paymentMethodSummary) {
        this.trainingTotalIncome = trainingTotalIncome;
        this.paymentMethodSummary = paymentMethodSummary;
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
}

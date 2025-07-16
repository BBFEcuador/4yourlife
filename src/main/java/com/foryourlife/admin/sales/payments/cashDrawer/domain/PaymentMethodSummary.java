package com.foryourlife.admin.sales.payments.cashDrawer.domain;

import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;

import java.io.Serializable;


public class PaymentMethodSummary implements Serializable {
    private PaymentMethod paymentMethod;
    private double totalAmount;
    private int transactionCount;

    public PaymentMethodSummary(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.totalAmount = 0;
        this.transactionCount = 0;
    }

    public void addPayment(double amount) {
        this.totalAmount += amount;
        this.transactionCount++;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }
}

package com.foryourlife.admin.sales.payments.domain;

public class PaymentHistory {
    private String paymentMethod;
    private double amount;
    private String date;

    public PaymentHistory(String paymentMethod, double amount, String date) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

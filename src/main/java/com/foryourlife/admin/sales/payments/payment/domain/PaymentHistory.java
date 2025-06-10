package com.foryourlife.admin.sales.payments.payment.domain;

public class PaymentHistory {
    private String id;
    private String paymentMethod;
    private double amount;
    private String date;
    private String paymentMethodId;

    public PaymentHistory(String paymentMethod, double amount, String date, String paymentMethodId) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.date = date;
        this.paymentMethodId = paymentMethodId;
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

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

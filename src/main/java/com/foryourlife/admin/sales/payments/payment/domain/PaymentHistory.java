package com.foryourlife.admin.sales.payments.payment.domain;

import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;

public class PaymentHistory {
    private String id;
    private PaymentMethod paymentMethod;
    private double amount;
    private String date;
    private String transactionId;
    private String pingType;
    private Boolean sent;

    public PaymentHistory(PaymentMethod paymentMethod, double amount, String date) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.date = date;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getPingType() {
        return pingType;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setPingType(String pingType) {
        this.pingType = pingType;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }
}

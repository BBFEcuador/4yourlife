package com.foryourlife.admin.sales.payments.payment.domain;

import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class PaymentHistory {
    private String id = UUID.randomUUID().toString();
    @NotNull
    private PaymentMethod paymentMethod;
    private double amount;
    private String date;
    private String transactionId;
    private String pingType;
    private Boolean sent = false;
    private String notSendError;
    private String creditCardType;
    private String paymentType;

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

    public String getNotSendError() {
        return notSendError;
    }

    public void setNotSendError(String notSendError) {
        this.notSendError = notSendError;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}

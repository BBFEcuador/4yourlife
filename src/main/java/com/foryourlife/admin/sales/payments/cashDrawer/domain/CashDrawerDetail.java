package com.foryourlife.admin.sales.payments.cashDrawer.domain;

import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

public class CashDrawerDetail {
    private String id;
    private String paymentId;
    private String paymentHistoryId;
    private String userId;

    protected CashDrawerDetail() {}

    public  CashDrawerDetail(String id, String paymentId, String paymentHistoryId) {
        this.id = id;
        this.paymentId = paymentId;
        this.paymentHistoryId = paymentHistoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentHistoryId() {
        return paymentHistoryId;
    }

    public void setPaymentHistoryId(String paymentHistoryId) {
        this.paymentHistoryId = paymentHistoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

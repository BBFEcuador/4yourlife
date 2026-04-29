package com.foryourlife.admin.sales.payments.cashDrawer.domain;

import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;

import java.io.Serializable;
import java.math.BigDecimal;


public class PaymentMethodSummary implements Serializable {
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
    private int transactionCount;

    public PaymentMethodSummary(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.totalAmount = BigDecimal.ZERO;
        this.transactionCount = 0;
    }

    public void addPayment(BigDecimal amount) {
        if (amount == null) return;
        this.totalAmount = this.totalAmount.add(amount);
        this.transactionCount++;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount == null ? BigDecimal.ZERO : totalAmount;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }
}

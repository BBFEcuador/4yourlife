package com.foryourlife.admin.sales.payments.cashDrawerDetail.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import jakarta.persistence.*;

@Entity
@Table(name = "cash_drawer_details")
public class CashDrawerDetail {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(
            name = "payment_id",
            referencedColumnName = "id"
    )
    private Payment payment;
    @Column(name = "payment_history_id", nullable = true)
    private String paymentHistoryId;
    @ManyToOne
    @JoinColumn(
            name = "cash_drawer_id",
            referencedColumnName = "id"
    )
    @JsonIgnoreProperties("cashDrawerDetails")
    private CashDrawer cashDrawer;

    protected CashDrawerDetail() {
    }

    public CashDrawerDetail(String id, Payment payment, String paymentHistoryId, CashDrawer cashDrawer) {
        this.id = id;
        this.payment = payment;
        this.paymentHistoryId = paymentHistoryId;
        this.cashDrawer = cashDrawer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getPaymentHistoryId() {
        return paymentHistoryId;
    }

    public void setPaymentHistoryId(String paymentHistoryId) {
        this.paymentHistoryId = paymentHistoryId;
    }

    public CashDrawer getCashDrawer() {
        return cashDrawer;
    }

    public void setCashDrawer(CashDrawer cashDrawer) {
        this.cashDrawer = cashDrawer;
    }
}

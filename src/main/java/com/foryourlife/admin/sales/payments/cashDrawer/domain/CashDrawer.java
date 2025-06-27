package com.foryourlife.admin.sales.payments.cashDrawer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.sales.payments.cashBox.domain.CashBox;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.application.CashDrawerDetailQueryService;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cash_drawers")
public class CashDrawer {
    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private CashDrawerStatus status;
    @ManyToOne
    @JoinColumn(
            name = "opened_by_user_id",
            referencedColumnName = "id"
    )
    private User openedByUser;
    @ManyToOne
    @JoinColumn(
            name = "closed_by_user_id",
            referencedColumnName = "id"
    )
    private User closedByUser;
    private LocalDateTime startDate;
    private LocalDateTime closeDate;
    @Column(
            name = "opening_balance"
    )
    private Double openingBalance;
    @Column(
            name = "closing_balance"
    )
    private Double closedBalance;
    @Column(
            name = "details"
    )
    private String detail;
    @JsonIgnoreProperties(
            "cashDrawer"
    )
    @ManyToOne
    @JoinColumn(
            name = "cash_box_id",
            referencedColumnName = "id"
    )
    private CashBox cashBox;
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDate created_at = LocalDate.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updated_at;
    @OneToMany(mappedBy = "cashDrawer", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("cashDrawer")
    private List<CashDrawerDetail> cashDrawerDetails;

    protected CashDrawer() {}

    public CashDrawer(String id, CashDrawerStatus status, User openedByUser, User closedByUser, LocalDateTime startDate, LocalDateTime closeDate, Double openingBalance, Double closedBalance, String detail, CashBox cashBox) {
        this.id = id;
        this.status = status;
        this.openedByUser = openedByUser;
        this.closedByUser = closedByUser;
        this.startDate = startDate;
        this.closeDate = closeDate;
        this.openingBalance = openingBalance;
        this.closedBalance = closedBalance;
        this.detail = detail;
        this.cashBox = cashBox;
    }

    public static CashDrawer create(String id, CashDrawerStatus status, User openedByUser, User closedByUser, LocalDateTime startDate, LocalDateTime closeDate, Double openingBalance, Double closedBalance, String detail, CashBox cashBox) {
        return new CashDrawer(id, status, openedByUser, closedByUser, startDate, closeDate, openingBalance, closedBalance, detail, cashBox);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CashDrawerStatus getStatus() {
        return status;
    }

    public void setStatus(CashDrawerStatus status) {
        this.status = status;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public User getOpenedByUser() {
        return openedByUser;
    }

    public void setOpenedByUser(User openedByUser) {
        this.openedByUser = openedByUser;
    }

    public User getClosedByUser() {
        return closedByUser;
    }

    public void setClosedByUser(User closedByUser) {
        this.closedByUser = closedByUser;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDateTime closeDate) {
        this.closeDate = closeDate;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Double getClosedBalance() {
        return closedBalance;
    }

    public void setClosedBalance(Double closedBalance) {
        this.closedBalance = closedBalance;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public CashBox getCashBox() {
        return cashBox;
    }

    public void setCashBox(CashBox cashBox) {
        this.cashBox = cashBox;
    }

    public List<CashDrawerDetail> getCashDrawerDetails() {
        return cashDrawerDetails;
    }

    public Double getActualBalance() {
        Double totalPayments = 0.0;
        if (this.cashDrawerDetails==null || this.cashDrawerDetails.isEmpty()){
            return this.openingBalance;
        }
        for (CashDrawerDetail detail : this.cashDrawerDetails) {
            Payment payment = detail.getPayment();
            if (payment != null) {
                for (PaymentHistory paymentHistory : payment.getPaymentshistory()) {
                    if (paymentHistory.getId().equals(detail.getPaymentHistoryId())) {
                        totalPayments += paymentHistory.getAmount();
                    }
                }
            }
        }
        return this.openingBalance + totalPayments;
    }
}
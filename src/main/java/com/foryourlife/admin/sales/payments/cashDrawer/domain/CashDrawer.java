package com.foryourlife.admin.sales.payments.cashDrawer.domain;

import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_drawers")
public class CashDrawer {
    @Id
    private String id;
    private String number;
    private Boolean isActive;
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
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant created_at = Instant.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updated_at;

    @ManyToOne
    @JoinColumn(
            name = "created_by_user",
            referencedColumnName = "id",
            nullable = false
    )
    private User createdBy;

    protected CashDrawer() {}

    public CashDrawer(String id, String number, Boolean isActive, CashDrawerStatus status, User openedByUser, User closedByUser, LocalDateTime startDate, LocalDateTime closeDate, Double openingBalance, Double closedBalance, String detail, User createdBy) {
        this.id = id;
        this.number = number;
        this.isActive = isActive;
        this.status = status;
        this.openedByUser = openedByUser;
        this.closedByUser = closedByUser;
        this.startDate = startDate;
        this.closeDate = closeDate;
        this.openingBalance = openingBalance;
        this.closedBalance = closedBalance;
        this.detail = detail;
        this.createdBy = createdBy;
    }

    public static CashDrawer create(String id, String number, Boolean isActive, CashDrawerStatus status, User openedByUser, User closedByUser, LocalDateTime startDate, LocalDateTime closeDate, Double openingBalance, Double closedBalance, String detail, User createdBy) {
        return new CashDrawer(id, number, isActive, status, openedByUser, closedByUser, startDate, closeDate, openingBalance, closedBalance, detail, createdBy);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public CashDrawerStatus getStatus() {
        return status;
    }

    public void setStatus(CashDrawerStatus status) {
        this.status = status;
    }

    public Instant getCreated_at() {
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}

package com.foryourlife.admin.sales.payments.cashDrawer.domain;

import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.shared.domain.user.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
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
    private String number;
    private Boolean isActive;
    private Boolean isOpen;
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
    @Column(columnDefinition = "jsonb", name = "cash_drawer_detail")
    @Type(JsonType.class)
    private List<CashDrawerDetail> cashDrawerDetail;

    protected CashDrawer() {}

    public CashDrawer(String id, String number, Boolean isActive, Boolean isOpen, User openedByUser, User closedByUser, LocalDateTime startDate, LocalDateTime closeDate, Double openingBalance, Double closedBalance, String detail, User createdBy) {
        this.id = id;
        this.number = number;
        this.isActive = isActive;
        this.isOpen = isOpen;
        this.openedByUser = openedByUser;
        this.closedByUser = closedByUser;
        this.startDate = startDate;
        this.closeDate = closeDate;
        this.openingBalance = openingBalance;
        this.closedBalance = closedBalance;
        this.detail = detail;
        this.createdBy = createdBy;
    }

    public static CashDrawer create(String id, String number, Boolean isActive, Boolean isOpen, User openedByUser, User closedByUser, LocalDateTime startDate, LocalDateTime closeDate, Double openingBalance, Double closedBalance, String detail, User createdBy) {
        return new CashDrawer(id, number, isActive, isOpen, openedByUser, closedByUser, startDate, closeDate, openingBalance, closedBalance, detail, createdBy);
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

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
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

    public List<CashDrawerDetail> getCashDrawerDetail() {
        return cashDrawerDetail;
    }

    public void setCashDrawerDetail(List<CashDrawerDetail> cashDrawerDetail) {
        this.cashDrawerDetail = cashDrawerDetail;
    }
}

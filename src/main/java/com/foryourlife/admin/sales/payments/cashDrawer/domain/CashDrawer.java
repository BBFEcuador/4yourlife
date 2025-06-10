package com.foryourlife.admin.sales.payments.cashDrawer.domain;

import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class CashDrawer {
    @Id
    private String id;
    private String number;
    private Boolean isActive;
    private Boolean isOpen;
    @ManyToOne
    @JoinColumn(
            name = "opened_by_user_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User openedByUser;
    @ManyToOne
    @JoinColumn(
            name = "closed_by_user_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User closedByUser;
    private LocalDate startDate;
    private LocalDate closeDate;
    private Double openingBalance;
    private Double closedBalance;
    private String detail;

    protected CashDrawer() {}

    public CashDrawer(String id, String number, Boolean isActive, Boolean isOpen, User openedByUser, User closedByUser, LocalDate startDate, LocalDate closeDate, Double openingBalance, Double closedBalance, String detail) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
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
}

package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.httpControllers;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.shared.domain.user.User;
import io.swagger.v3.oas.models.media.EmailSchema;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CashDrawerRequest {
    public String id;
    @NotNull(message = "The number field is required")
    @NotBlank(message = "The number field is required")
    public String number;
    public User openedByUser;
    public User closedByUser;
    public LocalDate startDate;
    public LocalDateTime closeDate;
    @NotNull(message = "The openingBalance field is required")
    public Double openingBalance;
    public Double closedBalance;
    @NotNull(message = "The user field is required")
    public User user;

    public String detail;

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public User getOpenedByUser() {
        return openedByUser;
    }

    public User getClosedByUser() {
        return closedByUser;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDateTime getCloseDate() {
        return closeDate;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public Double getClosedBalance() {
        return closedBalance;
    }

    public String getDetail() {
        return detail;
    }

    public CashDrawer toDomain() {
        return CashDrawer.create(id != null ? id : UUID.randomUUID().toString(), number, true, CashDrawerStatus.NEW, openedByUser, closedByUser, LocalDateTime.now(), closeDate, openingBalance, closedBalance, detail, user);
    }
}

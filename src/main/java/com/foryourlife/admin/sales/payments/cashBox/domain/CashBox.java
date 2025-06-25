package com.foryourlife.admin.sales.payments.cashBox.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cash_boxes")
public class CashBox {
    @Id
    private String id;
    private String number;

    private Boolean isActive;
    @ManyToOne
    @JoinColumn(
            name = "created_by_user",
            referencedColumnName = "id",
            nullable = false
    )
    private User createdBy;

    @CreatedDate
    @Column(
            name = "created_at",
            updatable = false
    )
    private LocalDateTime created_at = LocalDateTime.now();
    @JsonIgnore
    @OneToMany(mappedBy = "cashBox", fetch = FetchType.EAGER)
    public List<CashDrawer> cashDrawer;

    protected CashBox() {
    }

    public CashBox(String id, String number, Boolean is_active, User createdBy) {
        this.id = id;
        this.number = number;
        this.isActive = is_active;
        this.createdBy = createdBy;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public static CashBox create(String id, String number, Boolean is_active, User createdBy) {
        return new CashBox(id, number, is_active, createdBy);
    }
}

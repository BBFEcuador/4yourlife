package com.foryourlife.admin.sales.payments.cashBox.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.store.domain.Store;
import com.foryourlife.shared.infrastructure.auditable.AuditableEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cash_boxes")
public class CashBox extends AuditableEntity {
    @Id
    private String id;
    private String number;
    private Boolean isActive;
    @ManyToOne
    @JoinColumn(
            name = "store_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Store store;

    @Column(name = "first_number_invoice")
    private Integer firstNumberInvoice;
    @JsonIgnore
    @OneToMany(mappedBy = "cashBox", fetch = FetchType.EAGER)
    public List<CashDrawer> cashDrawer;

    protected CashBox() {
    }

    public CashBox(String id, String number, Boolean isActive, Integer firstNumberInvoice, Store store) {
        this.id = id;
        this.number = number;
        this.isActive = isActive;
        this.store = store;
        this.firstNumberInvoice = firstNumberInvoice;
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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Integer getFirstNumberInvoice() {
        return firstNumberInvoice;
    }

    public void setFirstNumberInvoice(Integer firstNumberInvoice) {
        this.firstNumberInvoice = firstNumberInvoice;
    }

    public static CashBox create(String id, String number, Boolean isActive, Integer firstNumberInvoice, Store store) {
        return new CashBox(id, number, isActive, firstNumberInvoice, store);
    }

    @JsonProperty(value = "opened", access = JsonProperty.Access.READ_ONLY)
    public Boolean isOpened() {
        for (CashDrawer drawer : this.cashDrawer) {
            if (drawer.getStatus() == com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus.OPEN ||
                drawer.getStatus() == com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus.LOCKED) {
                return true;
            }
        }
        return false;
    }
}

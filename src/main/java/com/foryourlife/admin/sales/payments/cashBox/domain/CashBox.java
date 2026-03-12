package com.foryourlife.admin.sales.payments.cashBox.domain;

import com.foryourlife.admin.sales.payments.store.domain.Store;
import com.foryourlife.shared.infrastructure.auditable.AuditableEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cash_boxes")
public class CashBox extends AuditableEntity {
    @Id
    private String id;
    private String number;
    private Boolean isActive;

    @Column(name = "is_opened")
    private Boolean isOpened;

    @ManyToOne
    @JoinColumn(
            name = "store_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Store store;

    @Column(name = "first_number_invoice")
    private Integer firstNumberInvoice;

    @Column(name = "opened_by_user")
    private String openedByUser;

    protected CashBox() {
    }

    public CashBox(String id, String number, Boolean isActive, Boolean isOpened, Integer firstNumberInvoice, Store store) {
        this.id = id;
        this.number = number;
        this.isActive = isActive;
        this.isOpened = isOpened;
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
        return new CashBox(id, number, isActive, false , firstNumberInvoice, store);
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getOpened() {
        return isOpened;
    }

    public void setOpened(Boolean opened) {
        isOpened = opened;
    }

    public String getOpenedByUser() {
        return openedByUser;
    }

    public void setOpenedByUser(String openedByUser) {
        this.openedByUser = openedByUser;
    }
}

package com.foryourlife.admin.sales.payments.paymentMethod.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod {
    @Id
    private String id;
    private String type;
    private Boolean isActive;

    protected PaymentMethod() {
    }

    public PaymentMethod(String id, String type, Boolean isActive) {
        this.id = id;
        this.type = type;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty(value = "isActive")
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public static PaymentMethod create(String id, String type, Boolean isActive){
        return new PaymentMethod(id, type, isActive);
    }
}

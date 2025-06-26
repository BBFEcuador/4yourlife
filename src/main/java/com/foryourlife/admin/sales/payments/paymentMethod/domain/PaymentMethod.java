package com.foryourlife.admin.sales.payments.paymentMethod.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import jakarta.persistence.*;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod {
    @Id
    private String id;
    private String type;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
    private SriPaymentMethod code;

    protected PaymentMethod() {
    }

    public PaymentMethod(String id, String type, Boolean isActive, SriPaymentMethod code) {
        this.id = id;
        this.type = type;
        this.isActive = isActive;
        this.code = code;
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

    public SriPaymentMethod getCode() {
        return code;
    }

    public void setCode(SriPaymentMethod code) {
        this.code = code;
    }

    public static PaymentMethod create(String id, String type, Boolean isActive, SriPaymentMethod code) {
        return new PaymentMethod(id, type, isActive, code);
    }
}

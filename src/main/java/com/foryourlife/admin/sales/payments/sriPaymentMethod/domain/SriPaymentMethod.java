package com.foryourlife.admin.sales.payments.sriPaymentMethod.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sri_payment_methods" )
public class SriPaymentMethod {
    @Id
    private String id;
    private String method;
    private String name;
    private String code;

    protected SriPaymentMethod() {}

    public SriPaymentMethod(String id, String method, String description, String code) {
        this.id = id;
        this.method = method;
        this.name = description;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String description) {
        this.name = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

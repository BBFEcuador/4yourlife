package com.foryourlife.admin.sales.discounts.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "product_discounts")
@Entity
public class ProductDiscount {
    @Id
    private String id;
    private String name;
    @Column(name = "discount_type")
    private String discountType;
    @Column(name = "discount_value")
    private Float discountValue;
    @Column(name = "need_supervision")
    private Boolean needSupervision;
    @Column(name = "is_active")
    private Boolean isActive;

    protected ProductDiscount() {
    }

    private ProductDiscount(String id, String name, String discountType, Float discountValue, Boolean needSupervision, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.needSupervision = needSupervision;
        this.isActive = isActive;
    }

    public static ProductDiscount create(String id, String name, String discountType, Float discountValue, Boolean needSupervision, Boolean isActive) {
        return new ProductDiscount(id, name, discountType, discountValue, needSupervision, isActive);
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscountType() {
        return discountType;
    }

    public Float getDiscountValue() {
        return discountValue;
    }

    public Boolean getNeedSupervision() {
        return needSupervision;
    }
    @JsonProperty("isActive")
    public Boolean getIsActive() {
        return isActive;
    }

    public void changeStatus(Boolean status) {
        this.isActive = status;
    }
}

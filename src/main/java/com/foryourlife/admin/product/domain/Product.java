package com.foryourlife.admin.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "products")
public class Product {
    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    private Boolean isActive;

    private String description;

    @NotNull
    private Double minValue;

    @NotNull
    private int taxPercentage;

    @NotNull
    private String type;

    @NotNull
    private String category_id;

    @NotNull
    private Double price_1;

    @NotNull
    private Double price_2;

    @NotNull
    private Double price_3;

    @NotNull
    private Double price_4;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updated_at;

    protected Product() {

    }

    public Product(String id, String name, String code, Boolean isActive, String description, Double minValue, int taxPercentage, String type, String category_id, Double price_1, Double price_2, Double price_3, Double price_4, Instant created_at, Instant updated_at) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.isActive = isActive;
        this.description = description;
        this.minValue = minValue;
        this.taxPercentage = taxPercentage;
        this.type = type;
        this.category_id = category_id;
        this.price_1 = price_1;
        this.price_2 = price_2;
        this.price_3 = price_3;
        this.price_4 = price_4;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public int getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(int taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public Double getPrice_1() {
        return price_1;
    }

    public void setPrice_1(Double price_1) {
        this.price_1 = price_1;
    }

    public Double getPrice_2() {
        return price_2;
    }

    public void setPrice_2(Double price_2) {
        this.price_2 = price_2;
    }

    public Double getPrice_3() {
        return price_3;
    }

    public void setPrice_3(Double price_3) {
        this.price_3 = price_3;
    }

    public Double getPrice_4() {
        return price_4;
    }

    public void setPrice_4(Double price_4) {
        this.price_4 = price_4;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public Instant getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Instant updated_at) {
        this.updated_at = updated_at;
    }

    public static Product create(String id, String name, String code, Boolean isActive, String description, Double minValue, int taxPercentage, String type, String category_id, Double price_1, Double price_2, Double price_3, Double price_4, Instant created_at, Instant updated_at) {
        return new Product(id, name, code, isActive, description, minValue, taxPercentage, type, category_id, price_1, price_2, price_3, price_4,created_at, updated_at);
    }


}

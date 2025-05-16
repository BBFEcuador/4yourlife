package com.foryourlife.admin.sales.product.domain;

import com.foryourlife.admin.sales.programs.domain.Program;
import com.foryourlife.admin.sales.rules.domain.Rule;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    private String id;

    private String name;

    private String code;

    private Double basePrice;

    private String currency;

    @Column(name="is_active")
    private Boolean isActive;

    private String description;

    @OneToMany(mappedBy = "product", targetEntity = Rule.class)
    private List<Rule> rules;

    @OneToMany(mappedBy = "product", targetEntity = Program.class)
    private List<Program> programs;

    protected Product() {
    }

    public Product(String id, String name, String code, Boolean isActive, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.isActive = isActive;
        this.description = description;
    }

    public static Product create(String id, String name, String code, Boolean isActive, String description) {
        return new Product(id, name, code, isActive, description);
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

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
}

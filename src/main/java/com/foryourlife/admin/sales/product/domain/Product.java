package com.foryourlife.admin.sales.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.sales.programs.domain.Program;
import com.foryourlife.admin.sales.rules.domain.Rule;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.ProductCreated;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends AggregateRoot implements Serializable {
    @Id
    private String id;

    private String name;

    private String code;

    private Double basePrice;

    private String currency;

    @Column(name = "is_active")
    private Boolean isActive;

    private String description;

    private String contificoId;

    @OneToMany(mappedBy = "product", targetEntity = Rule.class, fetch = FetchType.EAGER)
    private List<Rule> rules;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "programs_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "program_id")
    )
    private List<Program> programs;
    @JsonIgnoreProperties
    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "campus_id",
            referencedColumnName = "id"
    )
    private Campus campus;

    protected Product() {
    }

    public Product(String id, String name, String code, Double basePrice, String currency, Boolean isActive, String description, List<Rule> rules, List<Program> programs, Campus campus, String contificoId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.basePrice = basePrice;
        this.currency = currency;
        this.isActive = isActive;
        this.description = description;
        this.rules = rules;
        this.programs = programs;
        this.campus = campus;
        this.contificoId = contificoId;
    }

    public static Product create(String id, String name, String code, Double basePrice, String currency, Boolean isActive, String description, List<Rule> rules, List<Program> programs, Campus campus, String contificoId) {
        var product = new Product(id, name, code, basePrice, currency, isActive, description, rules, programs, campus, contificoId);
        product.record(new ProductCreated(product));
        return product;
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

    @JsonProperty(value = "isActive")
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

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    public String getContificoId() {
        return contificoId;
    }

    public void setContificoId(String contificoId) {
        this.contificoId = contificoId;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
}

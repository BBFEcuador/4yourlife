package com.foryourlife.admin.sales.rules.domain;

import com.foryourlife.admin.sales.product.domain.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "rules")
public class Rule {
    @Id
    private String id;

    private String description;

    private Double value;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Product product;
    //and many more

    protected Rule() {
    }

    private Rule(String id, String description, Double value) {
        this.id = id;
        this.description = description;
        this.value = value;
    }

    public Rule create(String id, String description, Double value){
        return new Rule(id, description, value);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}

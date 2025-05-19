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

    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Product product;
    //and many more

    protected Rule() {
    }

    private Rule(String id, String description, Double value, Boolean enabled, Product product) {
        this.id = id;
        this.description = description;
        this.value = value;
        this.enabled = enabled;
        this.product = product;
    }

    public Rule create(String id, String description, Double value,Boolean enabled, Product product){
        return new Rule(id, description, value, enabled, product );
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

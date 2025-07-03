package com.foryourlife.admin.sales.product.infrastructure.httpControllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.programs.domain.Program;
import com.foryourlife.admin.sales.rules.domain.Rule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class ProductRequest {
    private String id;

    @NotBlank(message = "Campo nombre no puede estar vacio" )
    private String name;

    @NotBlank(message = "Campo codigo no puede estar vacio" )
    private String code;

    @NotNull(message = "Campo precio base no puede estar vacio" )
    private Double basePrice;

    @NotBlank(message = "Campo moneda no puede estar vacio" )
    private String currency;

    @NotBlank(message = "Campo descripcion no puede estar vacio" )
    private String description;

    private List<Rule> rules;

    @NotNull(message = "Campo nombre no puede estar vacio" )
    private List<Program> programs;

    @NotNull(message = "Campo activo no puede estar vacio" )
    @JsonProperty(value = "isActive")
    private Boolean isActive;

    private Campus campus;

    public Product toDomain() {
        return Product.create(id!=null ? id: UUID.randomUUID().toString(), name, code, basePrice, currency, isActive, description, rules, programs, campus, null);
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}

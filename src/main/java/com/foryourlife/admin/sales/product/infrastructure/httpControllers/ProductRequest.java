package com.foryourlife.admin.sales.product.infrastructure.httpControllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.programs.domain.Program;
import com.foryourlife.admin.sales.rules.domain.Rule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class ProductRequest {
    public String id;

    @NotBlank(message = "Campo nombre no puede estar vacio")
    public String name;

    @NotBlank(message = "Campo codigo no puede estar vacio")
    public String code;

    @NotNull(message = "Campo precio base no puede estar vacio")
    public Double basePrice;

    @NotBlank(message = "Campo moneda no puede estar vacio")
    public String currency;

    @NotBlank(message = "Campo descripcion no puede estar vacio")
    public String description;

    public List<Rule> rules;

    @NotNull(message = "Campo programas no puede estar vacio")
    public List<Program> programs;

    @NotNull(message = "Campo activo no puede estar vacio")
    @JsonProperty(value = "isActive")
    public Boolean isActive;

    public Campus campus;

    public String contificoId;

    public Product toDomain() {
        return Product.create(id != null ? id : UUID.randomUUID().toString(), name, code, basePrice, currency, isActive, description, rules, programs, campus, contificoId);
    }
}

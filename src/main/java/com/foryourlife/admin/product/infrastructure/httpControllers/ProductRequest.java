package com.foryourlife.admin.product.infrastructure.httpControllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;

public class ProductRequest {
    @NotBlank(message = "Campo nombre no puede estar vacio" )
    private String name;

    @NotBlank(message = "Campo codigo no puede estar vacio" )
    private String code;

    @NotBlank(message = "Campo estado no puede estar vacio" )
    private String state;

    @NotBlank(message = "Campo descripcion no puede estar vacio" )
    private String description;

    @NotBlank(message = "Campo valor minimo no puede estar vacio" )
    private Double minValue;

    @NotBlank(message = "Campo nombre no puede estar vacio" )
    private Double maxValue;

    @NotBlank(message = "Campo nombre no puede estar vacio" )
    private String type;

    @NotBlank(message = "Campo nombre no puede estar vacio" )
    private String category_id;

    @NotBlank(message = "Campo nombre no puede estar vacio" )
    private Double price_1;

    @NotBlank(message = "Campo nombre no puede estar vacio" )
    private Double price_2;

    @NotBlank(message = "Campo nombre no puede estar vacio" )
    private Double price_3;

    @NotBlank(message = "Campo nombre no puede estar vacio" )
    private Double price_4;
}

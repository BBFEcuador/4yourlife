package com.foryourlife.admin.contifico.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class ProductContifico {
    @Id
    private String id;
    private String name;
    private String code;
    private Integer taxPercentage;
    private List<String> prices;
}

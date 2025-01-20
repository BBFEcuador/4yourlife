package com.foryourlife.payments.plan.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Plan {

    @Id
    private String id;
    private String image;
    private String name;
    private float price;

    protected Plan() {
    }

    private Plan(String id, String image, String name, float price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public static Plan create(String id, String image, String name, float price) {
        var plan = new Plan(id, image, name, price);
        return plan;
    }


    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }
}

package com.foryourlife.payments.plan.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Plan {

    @Id
    private String id;
    private String image;
    private String name;
    private String type;
    private String vouchers;
    private String userOnline;
    private boolean inventory;
    private boolean isStarted;
    private boolean pos;
    private float price;

    protected Plan() {
    }

    private Plan(String id, String image, String name, String type, String vouchers, String userOnline, boolean inventory, boolean isStarted, boolean pos, float price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.type = type;
        this.vouchers = vouchers;
        this.userOnline = userOnline;
        this.inventory = inventory;
        this.isStarted = isStarted;
        this.pos = pos;
        this.price = price;
    }

    public static Plan create(String id, String image, String name, String type, String vouchers, String userOnline, boolean inventory, boolean isStarted, boolean pos, float price) {
        var plan = new Plan(id, image, name, type, vouchers, userOnline, inventory, isStarted, pos, price);
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

    public String getType() {
        return type;
    }

    public String getVouchers() {
        return vouchers;
    }

    public String getUserOnline() {
        return userOnline;
    }

    public boolean isInventory() {
        return inventory;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isPos() {
        return pos;
    }

    public float getPrice() {
        return price;
    }
}

package com.foryourlife.admin.programs.campus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.contifico.config.domain.ConfigContifico;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "campus")
public class Campus implements Serializable {

    @Id
    private String id;
    private String country;
    private String city;
    private String address;
    private String phone;

    protected Campus() {
    }

    private Campus(String id, String country, String city, String address, String phone) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.address = address;
        this.phone = phone;
    }

    public static Campus create(String id, String country, String city, String address, String phone) {
        return new Campus(id, country, city, address, phone);
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}

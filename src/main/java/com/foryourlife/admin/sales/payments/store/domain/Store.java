package com.foryourlife.admin.sales.payments.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.campus.domain.Campus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Store {
    @Id
    private String id;
    private String address;
    private String number;
    @JsonIgnoreProperties
    @ManyToOne
    @JoinColumn(
            name = "campus_id",
            referencedColumnName = "id"
    )
    private Campus campus;

    protected Store() {}

    public Store(String id, String address, String number, Campus campus) {
        this.id = id;
        this.address = address;
        this.number = number;
        this.campus = campus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
}

package com.foryourlife.clients.account.phone.domain;

import com.foryourlife.clients.account.user.domain.Participant;
import jakarta.persistence.*;

@Entity
@Table(name = "phones")
public class Phone {
    @Id
    private String id;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Participant user;

    protected Phone() {
    }

    public Phone(String id, String phone, Participant user) {
        this.id = id;
        this.phone = phone;
        this.user = user;
    }

    public static Phone create(String id, String phone, Participant user) {
        return new Phone(id, phone, user);
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public Participant getUser() {
        return user;
    }
}

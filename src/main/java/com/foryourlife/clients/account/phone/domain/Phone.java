package com.foryourlife.clients.account.phone.domain;

import com.foryourlife.clients.account.user.domain.Users;
import jakarta.persistence.*;

@Entity
@Table(name = "phones")
public class Phone {
    @Id
    private String id;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    protected Phone() {
    }

    public Phone(String id, String phone, Users user) {
        this.id = id;
        this.phone = phone;
        this.user = user;
    }

    public static Phone create(String id, String phone, Users user) {
        return new Phone(id, phone, user);
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public Users getUser() {
        return user;
    }
}

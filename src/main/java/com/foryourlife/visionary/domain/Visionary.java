package com.foryourlife.visionary.domain;

import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "visionaries")
public class Visionary {
    @Id
    private String id;
    private String role;
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    protected Visionary() {
    }

    private Visionary(String id, String role, User user) {
        this.id = id;
        this.role = role;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Visionary create(String id, String role, User user){
        return new Visionary(id, role, user);
    }
}

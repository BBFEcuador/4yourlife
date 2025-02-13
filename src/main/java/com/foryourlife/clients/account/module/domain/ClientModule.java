package com.foryourlife.clients.account.module.domain;

import com.foryourlife.clients.account.user.domain.Users;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Entity
@Table(name = "client_modules")
public class ClientModule {
    @Id
    private String id;
    private Boolean hasFocus;
    private Boolean hasYour;
    private Boolean hasLife;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    protected ClientModule() {
    }

    public ClientModule(String id, Boolean hasFocus, Boolean hasYour, Boolean hasLife, Users user) {
        this.id = id;
        this.hasFocus = hasFocus;
        this.hasYour = hasYour;
        this.hasLife = hasLife;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getHasFocus() {
        return hasFocus;
    }

    public void setHasFocus(Boolean hasFocus) {
        this.hasFocus = hasFocus;
    }

    public Boolean getHasYour() {
        return hasYour;
    }

    public void setHasYour(Boolean hasYour) {
        this.hasYour = hasYour;
    }

    public Boolean getHasLife() {
        return hasLife;
    }

    public void setHasLife(Boolean hasLife) {
        this.hasLife = hasLife;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public static ClientModule create(String id, Boolean hasFocus, Boolean hasYour, Boolean hasLife, Users user) {
        return new ClientModule(id, hasFocus, hasYour, hasLife, user);
    }
}

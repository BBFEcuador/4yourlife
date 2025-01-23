package com.foryourlife.admin.auth.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "admins_users")
@EntityListeners(AuditingEntityListener.class)
public class Admin {
    @Id
    private String id;

    private String name;

    private String email;

    private String password;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private AdminRole adminRole_id;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updated_at;


    protected Admin() {

    }

    public Admin(String id, String name, String email, String password, AdminRole adminRole_id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.adminRole_id = adminRole_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AdminRole getRole_id() {
        return adminRole_id;
    }

    public void setRole_id(AdminRole adminRole) {
        this.adminRole_id = adminRole;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}

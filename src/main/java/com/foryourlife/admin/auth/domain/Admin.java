package com.foryourlife.admin.auth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.admin.programs.campus.domain.Campus;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Set;


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
    @JsonProperty("role")
    private AdminRole role;

    @ManyToMany
    @JoinTable(
            name = "campus_admins",
            joinColumns = @JoinColumn(name = "admin_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "campus_id", referencedColumnName = "id"))
    private Set<Campus> campus;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updated_at;


    protected Admin() {
    }

    public Admin(String id, String name, String email, String password, AdminRole role, Set<Campus> campus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.campus = campus;
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

    public AdminRole getRole() {
        return role;
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

    public Set<Campus> getCampus() {
        return campus;
    }
}

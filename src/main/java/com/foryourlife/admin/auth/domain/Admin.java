package com.foryourlife.admin.auth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.AdminCreated;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Set;


@Entity
@Table(name = "admins_users")
@EntityListeners(AuditingEntityListener.class)
public class Admin extends AggregateRoot {
    @Id
    private String id;

    @OneToOne(cascade = CascadeType.ALL )
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;

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

private boolean isActive;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updated_at;


    protected Admin() {
    }

    public Admin(String id, User user, AdminRole role, Set<Campus> campus, boolean isActive) {
        this.id = id;
        this.user = user;
        this.role = role;
        this.campus = campus;
        this.isActive=isActive;
    }

    public static Admin create(String id, User user, AdminRole role, Set<Campus> campus,String plainPassword) {
        var admin = new Admin(id, user, role, campus, true);
        admin.record(new AdminCreated(id, admin,plainPassword));
        return admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return user.getName();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public AdminRole getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }

    public Set<Campus> getCampus() {
        return campus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package com.foryourlife.staff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "staffs")
public class Staff implements Serializable {
    @Id
    private String id;

    private String rol;

    private Boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(mappedBy = "staffs", targetEntity = Team.class, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"users", "trainer", "masterLife", "staffs"})
    private Set<Team> teams = new HashSet<>();

    protected Staff() {

    }

    private Staff(String id, String rol,Boolean isActive, User user) {
        this.id = id;
        this.rol = rol;
        this.isActive = isActive;
        this.user = user;
    }

    public static Staff create(String id, String rol, Boolean isActive, User user){
        return new Staff(id, rol, isActive, user);
    }

    public String getId() {
        return id;
    }

    public String getRol() {
        return rol;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public User getUser() {
        return user;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void changeStatus(){
        this.isActive = !this.isActive;
    }

    @JsonProperty(access =  JsonProperty.Access.READ_ONLY)
    public Boolean getIsCaptain() {
        return "CAPITAN".equalsIgnoreCase(this.rol);
    }
}

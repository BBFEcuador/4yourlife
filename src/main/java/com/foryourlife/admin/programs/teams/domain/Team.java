package com.foryourlife.admin.programs.teams.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Users;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.TeamCreated;
import com.foryourlife.shared.domain.events.TeamToTrainingAssigned;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team extends AggregateRoot{

    @Id
    private String id;
    private String name;
    private String photo;
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "training_id", referencedColumnName = "id", updatable = false)
    private Training training;
    @Column(name = "training_number")
    private Integer trainingNumber;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "team_users",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private Set<Users> users = Collections.emptySet();
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "team_master_life",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "masterlife_id", referencedColumnName = "id"))
    private Set<Users> masterLife = Collections.emptySet();

    protected Team() {
    }

    private Team(String id, String name, String photo, Training trainingId, Integer trainingNumber, Set<Users> users, Set<Users> masterLife) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.training = trainingId;
        this.trainingNumber = trainingNumber;
        this.users = users;
        this.masterLife = masterLife;
    }

    public static Team create(String id, String name, String photo, Training trainingId, Integer trainingNumber, Set<Users> users, Set<Users> masterLife) {
        var team = new Team(id, name, photo, trainingId, trainingNumber, users, masterLife);
        team.setTraining(trainingId);
        team.record(new TeamCreated(id, team));
        return team;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public Training getTraining() {
        return training;
    }

    public Integer getTrainingNumber() {
        return trainingNumber;
    }

    public Set<Users> getUsers() {
        return users;
    }

    public Set<Users> getMasterLife() {
        return masterLife;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }

    public void setMasterLife(Set<Users> masterLife) {
        this.masterLife = masterLife;
    }

    public void setTraining(Training newTraining) {
        this.training = newTraining;
        this.record(new TeamToTrainingAssigned(this.id, this,newTraining));
    }

    @Override
    public String toString() {
        return "Team{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", trainingNumber=" + trainingNumber +
                ", users=" + users +
                ", masterLife=" + masterLife +
                '}';
    }
}

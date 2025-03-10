package com.foryourlife.admin.programs.teams.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.TeamCreated;
import com.foryourlife.shared.domain.events.TeamToTrainingAssigned;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@Entity
@Table(name = "teams")
public class Team extends AggregateRoot{
    @Id
    private String id;
    private String name;
    private String photo;
    @ManyToOne()
    @JoinColumn(name = "training_id", referencedColumnName = "id")
    private Training training;
    @Column(name = "training_number")
    private Integer trainingNumber;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "team_users",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private List<Participant> users = new ArrayList<>();
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "team_master_life",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "masterlife_id", referencedColumnName = "id"))
    private List<Participant> masterLife = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "id")
    @JsonManagedReference
    private Trainer trainer;

    @Transient
    @Value("${api.url}")
    private String baseUrl;

    protected Team() {
    }

    private Team(String id, String name, String photo, Training trainingId, Integer trainingNumber, List<Participant> users,Trainer trainer) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.training = trainingId;
        this.trainingNumber = trainingNumber;
        this.users = users;
        this.trainer = trainer;
    }

    public static Team create(String id, String name, String photo, Training trainingId, Integer trainingNumber, List<Participant> users,Trainer trainer) {
        var team = new Team(id, name, photo, trainingId, trainingNumber, users,trainer);
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
        return (photo != null && !photo.isEmpty()) ? baseUrl + photo : baseUrl + "resources/assets/teamPhotos/DefaultTeam.png";
    }

    public Training getTraining() {
        return training;
    }

    public Integer getTrainingNumber() {
        return trainingNumber;
    }
    @JsonIgnore
    public List<Participant> getUsers() {
        return users;
    }

    public List<Participant> getMasterLife() {
        return masterLife;
    }

    public void setUsers(List<Participant> users) {
        this.users = users;
    }

    public void setMasterLife(List<Participant> masterLife) {
        this.masterLife = masterLife;
    }

    public void setTraining(Training newTraining) {
        this.training = newTraining;
        this.record(new TeamToTrainingAssigned(this.id, this,newTraining));
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Trainer getTrainer() {
        return trainer;
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

    public Map<String,String> getTrainingData(){
        return new HashMap<String,String>(){{
            put("startDate",training.getStartDate().toString());
            put("endDate",training.getEndDate().toString());
            put("name",training.getName());
            put("curseLevel",training.getCourseLevel().name());
            put("sede",training.getCampus().getCity());
        }};
    }
}

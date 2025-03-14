package com.foryourlife.admin.programs.teams.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.TeamCreated;
import com.foryourlife.shared.domain.events.TeamToTrainingAssigned;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.visionary.domain.Visionary;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "teams")
public class Team extends AggregateRoot implements Serializable {
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
    @JsonIgnoreProperties("team")
    private List<Participant> users = new ArrayList<>();
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "team_master_life",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "masterlife_id", referencedColumnName = "id"))
    @JsonIgnoreProperties("team")
    private List<Participant> masterLife = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "id")
    private Trainer trainer;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "team_staff",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id", referencedColumnName = "id"))
    private List<Staff> staffs = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "team_visionary",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "visionary_id", referencedColumnName = "id"))
    private List<Visionary> visionaries = new ArrayList<>();

    protected Team() {
    }

    private Team(String id, String name, String photo, Training trainingId, Integer trainingNumber, List<Participant> users, Trainer trainer, List<Staff> staffs, List<Visionary> visionaries, List<Participant> masterLife) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.training = trainingId;
        this.trainingNumber = trainingNumber;
        this.users = users;
        this.trainer = trainer;
        this.staffs = staffs;
        this.visionaries = visionaries;
        this.masterLife = masterLife;
    }

    public static Team create(String id, String name, String photo, Training trainingId, Integer trainingNumber, List<Participant> users, Trainer trainer, List<Staff> staffs, List<Visionary> visionaries, List<Participant> masterLife) {
        var team = new Team(id, name, photo, trainingId, trainingNumber, users, trainer, staffs, visionaries, masterLife);
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
        return (photo != null && !photo.isEmpty()) ? photo : "api/resources/static/teamPhotos/DefaultTeam.png";
    }

    public Training getTraining() {
        return training;
    }

    public Integer getTrainingNumber() {
        return trainingNumber;
    }

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
        this.record(new TeamToTrainingAssigned(this.id, this, newTraining));
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public List<Staff> getStaffs() {
        return staffs;
    }

    public List<Visionary> getVisionaries() {
        return visionaries;
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

    public Map<String, String> getTrainingData() {
        return new HashMap<String, String>() {{
            put("startDate", training.getStartDate().toString());
            put("endDate", training.getEndDate().toString());
            put("name", training.getName());
            put("curseLevel", training.getCourseLevel().name());
            put("sede", training.getCampus().getCity());
        }};
    }
}

package com.foryourlife.staff.domain;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "staffs")
public class Staff {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "training_id", referencedColumnName = "id")
    private Training trainingId;
    @OneToOne
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    private Participant participant;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    protected Staff(){

    }

    public Staff(String id, Training trainingId, Participant participant, User user) {
        this.id = id;
        this.trainingId = trainingId;
        this.participant = participant;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Training getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Training trainingId) {
        this.trainingId = trainingId;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Staff create(String id, Training training, Participant participant, User user){
        return new Staff(id, training, participant, user);
    }
}

package com.foryourlife.clients.account.promises.domain;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.participant.domain.Participant;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "promises")
public class Promise {
    @Id
    private String id;
    private Integer firstPromise;
    private Integer secondPromise;
    private Integer thirdPromise;
    private Integer achievedCount;
    private Integer paidCount;
    @ManyToOne
    @JoinColumn(
        name = "participant_id",
        referencedColumnName = "id"
    )
    private Participant participant;
    @ManyToOne
    @JoinColumn(
        name = "training_id",
        referencedColumnName = "id"
    )
    private Training training;;

    protected Promise() {
    }

    public Promise(String id, Training training, Participant participant) {
        this.id = id;
        this.training = training;
        this.participant = participant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFirstPromise() {
        return firstPromise;
    }

    public void setFirstPromise(Integer firstPromise) {
        this.firstPromise = firstPromise;
    }

    public Integer getSecondPromise() {
        return secondPromise;
    }

    public void setSecondPromise(Integer secondPromise) {
        this.secondPromise = secondPromise;
    }

    public Integer getThirdPromise() {
        return thirdPromise;
    }

    public void setThirdPromise(Integer thirdPromise) {
        this.thirdPromise = thirdPromise;
    }

    public Integer getAchievedCount() {
        return achievedCount;
    }

    public void setAchievedCount(Integer achievedCount) {
        this.achievedCount = achievedCount;
    }

    public Integer getPaidCount() {
        return paidCount;
    }

    public void setPaidCount(Integer paidCount) {
        this.paidCount = paidCount;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }
}

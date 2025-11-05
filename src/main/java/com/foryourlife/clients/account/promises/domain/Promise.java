package com.foryourlife.clients.account.promises.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "promises")
public class Promise {
    @Id
    private String id;
    @Column(
            name = "first_promise"
    )
    private Integer firstPromise = 0;
    @Column(
            name = "second_promise"
    )
    private Integer secondPromise = 0;
    @Column(
            name = "third_promise"
    )
    private Integer thirdPromise = 0;
    @Column(
            name = "achieved_count"
    )
    private Integer achievedCount = 0;
    @Column(
            name = "paid_count"
    )
    private Integer paidCount = 0;
    @Column(
            name = "start_date"
    )
    private LocalDate startDate;
    @Column(
            name = "end_date"
    )
    private LocalDate endDate;
    @JsonIgnoreProperties(
            {"participantLevel", "campus", "modules", "contacts", "medicalRecord", "teams", "team"}
    )
    @ManyToOne
    @JoinColumn(
            name = "participant_id",
            referencedColumnName = "id"
    )
    private User user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(
            name = "training_id",
            referencedColumnName = "id"
    )
    private Training training;

    protected Promise() {
    }

    public Promise(String id, Training training, User user) {
        this.id = id;
        this.training = training;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User participant) {
        this.user = participant;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

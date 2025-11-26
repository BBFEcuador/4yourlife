package com.foryourlife.admin.crm.call.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.crm.callLogs.domain.CallLog;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "calls")
public class Call {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(
            name = "called_user_id",
            referencedColumnName = "id"
    )
    private User calledUser;

    @ManyToOne
    @JoinColumn(
            name = "training_id",
            referencedColumnName = "id"
    )
    private Training training;

    @OneToMany(
            mappedBy = "call",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties({"call"})
    private List<CallLog> callLogs = new ArrayList<>();

    protected Call() {
    }

    public Call(String id, User calledUser, Training training) {
        this.id = id;
        this.calledUser = calledUser;
        this.training = training;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCalledUser() {
        return calledUser;
    }

    public void setCalledUser(User calledUser) {
        this.calledUser = calledUser;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public List<CallLog> getCallLogs() {
        return callLogs;
    }

    public void setCallLogs(List<CallLog> callLogs) {
        this.callLogs = callLogs;
    }
}

package com.foryourlife.admin.statement.domain;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Participant;
import jakarta.persistence.*;

@Entity
@Table(name = "statements")
public class Statement {
    @Id
    private String id;
    @Column(updatable = false)
    private Integer statements;

    private Integer registered;

    private Integer paid;

    private Boolean isActive;

    @OneToOne(optional = true)
    @JoinColumn(referencedColumnName = "id", name = "user_id", nullable = true)
    private Participant users;

    @OneToOne(optional = true)
    @JoinColumn(referencedColumnName = "id", name = "training_id", nullable = true)
    private Training trainer;

    protected Statement() {
    }

    public Statement(String id, Integer statements, Integer registered, Integer paid, Boolean isActive, Participant users, Training trainer) {
        this.id = id;
        this.statements = statements;
        this.registered = registered;
        this.paid = paid;
        this.isActive = isActive;
        this.users = users;
        this.trainer = trainer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatements() {
        return statements;
    }

    public void setStatements(Integer statements) {
        this.statements = statements;
    }

    public Integer getRegistered() {
        return registered;
    }

    public void setRegistered(Integer registered) {
        this.registered = registered;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Participant getUsers() {
        return users;
    }

    public void setUsers(Participant users) {
        this.users = users;
    }

    public Training getTrainer() {
        return trainer;
    }

    public void setTrainer(Training trainer) {
        this.trainer = trainer;
    }

    public static Statement create(String id, Integer statements, Integer registered, Integer paid, Boolean isActive, Participant user, Training training) {
        return new Statement(id, statements, registered, paid, isActive, user, training);
    }
}

package com.foryourlife.admin.statement.infrastructure.httpControllers;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.statement.domain.Statement;
import com.foryourlife.clients.account.participant.domain.Participant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class StatementRequest {
    private String id;

    @NotNull
    @NotBlank(message = "¡La declaracion es requerida!")
    private Integer statement;

    private Integer registered;

    private Integer paid;

    private Boolean isActive;

    @NotNull()
    private Training training;

    @NotNull()
    private Participant user;

    public StatementRequest(String id, Integer statement, Integer registered, Integer paid, Boolean isActive, Training training, Participant user) {
        this.id = id;
        this.statement = statement;
        this.registered = registered;
        this.paid = paid;
        this.isActive = isActive;
        this.training = training;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatement() {
        return statement;
    }

    public void setStatement(Integer statement) {
        this.statement = statement;
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

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public Participant getUser() {
        return user;
    }

    public void setUser(Participant user) {
        this.user = user;
    }

    public Statement toDomain() {
        return Statement.create(id != null ? id : UUID.randomUUID().toString(), statement, registered != null ? registered : 0, paid != null ? paid : 0, isActive != null ? isActive : true, user, training);
    }
}

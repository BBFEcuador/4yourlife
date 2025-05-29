package com.foryourlife.clients.account.module.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.ClientModulesUpdated;
import jakarta.persistence.*;

@Entity
@Table(name = "client_modules")
public class ClientModule extends AggregateRoot {
    @Id
    private String id;
    private Boolean hasFocus;
    private Boolean hasYour;
    private Boolean hasLife;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Participant user;

    protected ClientModule() {
    }

    public ClientModule(String id, Boolean hasFocus, Boolean hasYour, Boolean hasLife, Participant user) {
        this.id = id;
        this.hasFocus = hasFocus;
        this.hasYour = hasYour;
        this.hasLife = hasLife;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getHasFocus() {
        return hasFocus;
    }

    public void setHasFocus(Boolean hasFocus) {
        this.hasFocus = hasFocus;
    }

    public Boolean getHasYour() {
        return hasYour;
    }

    public void setHasYour(Boolean hasYour) {
        this.hasYour = hasYour;
    }

    public Boolean getHasLife() {
        return hasLife;
    }

    public void setHasLife(Boolean hasLife) {
        this.hasLife = hasLife;
    }

    @JsonIgnore
    public Participant getUser() {
        return user;
    }

    public void setUser(Participant user) {
        this.user = user;
    }

    public static ClientModule create(String id, Boolean hasFocus, Boolean hasYour, Boolean hasLife, Participant user) {
        var clientModule = new ClientModule(id, hasFocus, hasYour, hasLife, user);
        clientModule.record(new ClientModulesUpdated(clientModule));
        return clientModule;
    }
}

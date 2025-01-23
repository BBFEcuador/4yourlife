package com.foryourlife.programs.domain;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "programs")
public class Program {
    @Id
    private String id;

    private String name;

    private int level;

    protected Program(){

    }

    public Program(String id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Program{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                '}';
    }

    public static Program create(String id, String name, int level) {
        return new Program(id, name, level);
    }
}

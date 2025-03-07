package com.foryourlife.shared.domain.user;

public class UserEntities {
    private String id;
    private String entity;

    public UserEntities(String id, String entity) {
        this.id = id;
        this.entity = entity;
    }

    protected UserEntities() {
    }

    public String getId() {
        return id;
    }

    public String getEntity() {
        return entity;
    }
}

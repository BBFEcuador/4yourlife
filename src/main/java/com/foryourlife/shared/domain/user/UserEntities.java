package com.foryourlife.shared.domain.user;

import java.io.Serializable;

public class UserEntities implements Serializable {
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

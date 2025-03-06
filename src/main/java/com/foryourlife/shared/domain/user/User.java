package com.foryourlife.shared.domain.user;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String name;
    private String phone;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<UserEntities> entityMap;
}

package com.foryourlife.shared.domain.user.infrastructure;

import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserEntities;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class SaveGeneraUserRequest {
    public String id;
    @NotNull
    @Email
    public String email;

    public String password;
    @NotNull
    public String name;
    @NotNull
    public String phone;

    public SaveGeneraUserRequest(String id, String email, String password, String name, String phone) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public User toDomain(List<UserEntities> entity){
        return new User(
                id != null ? id: UUID.randomUUID().toString(),
                email,
                password,
                name,
                phone,
                entity
        );
    }
}

package com.foryourlife.shared.domain.user.infrastructure;

import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserEntities;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotNull
    public String password;
    @NotNull
    @NotBlank(message = "The name1 field is required")
    public String name1;
    @NotNull
    @NotBlank(message = "The name2 field is required")
    public String name2;
    @NotNull
    @NotBlank(message = "The lastname1 field is required")
    public String lastname1;
    @NotNull
    @NotBlank(message = "The lastname2 field is required")
    public String lastname2;
    @NotNull
    public String name;
    @NotNull
    public String phone;

    public User toDomain(List<UserEntities> entity) {
        return new User(
                id != null ? id : UUID.randomUUID().toString(),
                email,
                password,
                name1,
                name2,
                lastname1,
                lastname2,
                name,
                phone,
                entity
        );
    }
}

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SaveGeneraUserRequest {
    public String id;
    @NotNull
    @Email
    public String email;
    @NotNull
    public String password;
    @NotNull
    @NotBlank(message = "El nombre es obligatorio")
    public String name1;
    public String name2;
    @NotNull
    @NotBlank(message = "El apellido es obligatorio")
    public String lastname1;
    public String lastname2;

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
                name = Stream.of(name1, name2, lastname1, lastname2)
                        .filter(s -> s != null && !s.trim().isEmpty())
                        .collect(Collectors.joining(" ")),
                phone,
                entity
        );
    }
}

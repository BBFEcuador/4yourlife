package com.foryourlife.admin.auth.infrastructure.httpControllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AdminLoginRequest {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Email(message = "El email no es valido")
    public String username;
    @NotBlank(message = "La contraseña no puede ser vacia")
    public String password;

    public AdminLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username.toLowerCase();
    }

    public String getPassword() {
        return password;
    }
}

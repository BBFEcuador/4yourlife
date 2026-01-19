package com.foryourlife.clients.account.participant.infrastructure.httpControllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResetPasswordRequest {
    @NotNull(message = "La nueva contraseña es requerida")
    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    public String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

package com.foryourlife.clients.account.participant.infrastructure.httpControllers;

public class ResetPasswordRequest {
    public String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

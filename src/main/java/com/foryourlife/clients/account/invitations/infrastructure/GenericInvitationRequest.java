package com.foryourlife.clients.account.invitations.infrastructure;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class GenericInvitationRequest {
    @NotNull
    public String id;
    @NotNull
    public String trainingId;
    @NotNull
    @Pattern(regexp = "\\d+")
    public String quantity;

    public GenericInvitationRequest(String id, String quantity, String trainingId) {
        this.id = id;
        this.quantity = quantity;
        this.trainingId = trainingId;
    }
}

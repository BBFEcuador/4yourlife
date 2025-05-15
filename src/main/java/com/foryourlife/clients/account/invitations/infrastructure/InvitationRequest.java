package com.foryourlife.clients.account.invitations.infrastructure;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class InvitationRequest {
    @NotNull
    public String id;
    @NotNull
    public String campusId;
    @NotNull
    @Pattern(regexp = "\\d+")
    public String quantity;

    public InvitationRequest(String id, String quantity, String campusId) {
        this.id = id;
        this.quantity = quantity;
        this.campusId = campusId;
    }
}

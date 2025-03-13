package com.foryourlife.clients.account.invitations.infrastructure;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class InvitationRequest {
    @NotNull
    public String id;
    @NotNull
    @Pattern(regexp = "\\d+")
    public String quantity;
}

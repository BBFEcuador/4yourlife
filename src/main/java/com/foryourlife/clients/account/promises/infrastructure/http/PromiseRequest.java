package com.foryourlife.clients.account.promises.infrastructure.http;

import jakarta.validation.constraints.NotNull;

public class PromiseRequest {
    public String id;
    @NotNull(message = "El valor de la promesa no puede ser nulo")
    public Integer promise;
}

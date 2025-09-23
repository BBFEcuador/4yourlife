package com.foryourlife.clients.account.promises.infrastructure.http;

import jakarta.validation.constraints.NotNull;

public class PromiseRequest {
    @NotNull(message = "El id de la declaracion no puede ser nulo")
    public String id;
    @NotNull(message = "El valor de la declaracion no puede ser nulo")
    public Integer promise;
}

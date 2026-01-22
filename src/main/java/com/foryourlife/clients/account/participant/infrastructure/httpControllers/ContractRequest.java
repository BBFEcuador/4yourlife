package com.foryourlife.clients.account.participant.infrastructure.httpControllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ContractRequest {
    @NotNull(message = "El ID del producto es obligatorio")
    @NotBlank(message = "El ID del entrenamiento no puede estar vacío")
    public String productId;
    @NotNull(message = "El ID del entrenamiento es obligatorio")
    @NotBlank(message = "El ID del entrenamiento no puede estar vacío")
    public String trainingId;
}

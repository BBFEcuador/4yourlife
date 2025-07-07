package com.foryourlife.admin.contifico.config.infrastructure.http;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ConfigContificoRequest {
    public String id;
    @NotNull(message = "campusId es requerido")
    @NotBlank(message = "campusId no puede estar vacío")
    public String campusId;
    @NotNull(message = "Api key es requerida")
    @NotBlank(message = "Api key no puede estar vacío")
    public String apiKey;
    @NotNull(message = "Api secret es requerida")
    @NotBlank(message = "Api secret no puede estar vacío")
    public String apiSecret;
}

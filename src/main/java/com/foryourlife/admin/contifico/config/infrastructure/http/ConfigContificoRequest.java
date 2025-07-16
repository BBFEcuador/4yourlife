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
    @NotNull(message = "Ruc es requerido")
    @NotBlank(message = "El RUC no puede estar vacío")
    public String ruc;
    @NotNull(message = "Razón social es requerida")
    @NotBlank(message = "La razón social no puede estar vacía")
    public String razonSocial;
    @NotNull(message = "Dirección es requerida")
    @NotBlank(message = "La dirección no puede estar vacía")
    public String address;
    @NotNull(message = "Telefono es requerido")
    @NotBlank(message = "El telefono no puede estar vacío")
    public String phone;

}

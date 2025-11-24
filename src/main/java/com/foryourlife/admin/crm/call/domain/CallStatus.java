package com.foryourlife.admin.crm.call.domain;

public enum CallStatus {
    DONE("Realizada"),
    NOT_ANSWERED("No Contestada"),
    RE_SCHEDULED("Reprogramada"),
    IN_PROGRESS("En Progreso");

    private final String value;

    CallStatus(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CallStatus fromValue(String value) {
        for (CallStatus status : CallStatus.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No existe el valor: " + value + " para el estado de la llamada");
    }
}

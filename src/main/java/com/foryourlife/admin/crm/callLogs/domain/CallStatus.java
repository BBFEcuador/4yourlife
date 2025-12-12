package com.foryourlife.admin.crm.callLogs.domain;

public enum CallStatus {
    DONE("Realizada"),
    NOT_ANSWERED("No Contestada"),
    NOT_INTERESTED("No Interesa"),
    ANOTHER_CAMPUS("Otra Sede"),
    NEXT_DATE("Proxima Fecha"),
    FOR_CONFIRMATION("Por confirmar"),
    CONFIRMED("Confirmado"),
    RE_SCHEDULED("Reprogramada");

    private final String value;

    CallStatus(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CallStatus fromValue(String value) {
        for (CallStatus status : CallStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No existe el valor: " + value + " para el estado de la llamada");
    }
}

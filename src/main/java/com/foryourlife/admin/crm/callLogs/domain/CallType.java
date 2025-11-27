package com.foryourlife.admin.crm.callLogs.domain;

public enum CallType {
    WELCOME("Bienvenida"),
    LOGISTIC("Logística");

    private final String value;

    CallType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CallType fromValue(final String value) {
        for (CallType type : CallType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No existe el valor: " + value + " para el tipo de llamada");
    }
}

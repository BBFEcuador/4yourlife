package com.foryourlife.shared.domain.user;

public enum UserType {
    STAFF("Staff"),
    PARTICIPANT("Participante"),
    ADMIN("Administrador"),
    VISIONARY("Visionario"),
    MASTER_LIFE("Master Life");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

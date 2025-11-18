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

    public static UserType fromValue(String value) {
        for (UserType type : UserType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}

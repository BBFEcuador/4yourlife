package com.foryourlife.shared.domain.level;

public enum CourseLevel {
    INIT("Iniciado"),
    FOCUS("Focus"),
    YOUR("Your"),
    LIFE("Life"),
    LIFE_2("Life 2"),
    LIFE_3("Life 3"),
    LIFE_GRADUATE("Graduado");

    private final String value;

    CourseLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

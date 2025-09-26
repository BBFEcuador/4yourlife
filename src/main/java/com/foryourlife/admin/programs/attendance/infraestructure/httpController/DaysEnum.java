package com.foryourlife.admin.programs.attendance.infraestructure.httpController;

public enum DaysEnum {
    FRIDAY, SATURDAY, SUNDAY;
    public static DaysEnum fromString(String value) {
        for (DaysEnum day : DaysEnum.values()) {
            if (day.name().equalsIgnoreCase(value)) {
                return day;
            }
        }
        throw new IllegalArgumentException("No enum constant with text " + value + " found");
    }
}

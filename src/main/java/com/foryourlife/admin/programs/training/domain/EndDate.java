package com.foryourlife.admin.programs.training.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.foryourlife.shared.domain.exception.BaseException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EndDate {
    private LocalDate value;

    protected EndDate() {
    }

    public EndDate(LocalDate value) {
        this.value = validate(value);
    }

    private LocalDate validate(LocalDate value){

        if (value.getDayOfWeek() != DayOfWeek.SUNDAY) {
            throw new BaseException("Error al actualizar la fecha de fin", List.of("La fecha de fin debe ser un domingo"));
        }
        return value;
    }

    @JsonValue
    public LocalDate getValue() {
        return value;
    }
    @JsonCreator
    public static EndDate fromValue(LocalDate value) {
        return new EndDate(value);
    }
}

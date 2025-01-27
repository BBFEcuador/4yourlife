package com.foryourlife.admin.programs.training.domain;

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
            throw new BaseException("Mal", List.of("La fecha final debe ser un domingo"));
        }
        return value;
    }

    public LocalDate getValue() {
        return value;
    }
}

package com.foryourlife.admin.programs.training.domain;

import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.persistence.Embeddable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Embeddable
public class StartDate {

    private LocalDate value;

    protected StartDate() {
    }

    public StartDate(LocalDate value) {
        this.value = validate(value);
    }

    private LocalDate validate(LocalDate value){
        if (value.getDayOfWeek() != DayOfWeek.FRIDAY) {
            throw new BaseException("Mal", List.of("La fecha de inicio debe ser un viernes"));
        }
        return value;
    }

    public LocalDate getValue() {
        return value;
    }
}

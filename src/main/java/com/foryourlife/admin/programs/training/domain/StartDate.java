package com.foryourlife.admin.programs.training.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.foryourlife.clients.account.profileDetails.domain.UserBirthday;
import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.persistence.Embeddable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
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
            throw new BaseException("Error al actualizar la fecha de inicio", List.of("La fecha de inicio debe ser viernes"));
        }
        return value;
    }
    @JsonValue
    public LocalDate getValue() {
        return value;
    }

    @JsonCreator
    public static StartDate fromValue(LocalDate value) {
        return new StartDate(value);
    }
}


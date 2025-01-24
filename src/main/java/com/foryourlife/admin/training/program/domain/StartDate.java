package com.foryourlife.admin.training.program.domain;

import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.persistence.Embeddable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Embeddable
public class StartDate {

    private Date value;

    protected StartDate() {
    }

    public StartDate(Date value) {
        this.value = validate(value);
    }

    private Date validate(Date value){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            throw new BaseException("Mal", List.of("La fecha de inicio debe ser un viernes"));
        }
        return value;
    }

    public Date getValue() {
        return value;
    }
}

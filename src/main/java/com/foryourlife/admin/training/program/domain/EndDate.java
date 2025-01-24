package com.foryourlife.admin.training.program.domain;

import com.foryourlife.shared.domain.exception.BaseException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EndDate {
    private Date value;

    protected EndDate() {
    }

    public EndDate(Date value) {
        this.value = validate(value);
    }

    private Date validate(Date value){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw new BaseException("Mal", List.of("La fecha de cierre debe ser un domingo"));
        }
        return value;
    }

    public Date getValue() {
        return value;
    }
}

package com.foryourlife.clients.account.profileDetails.domain;

import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Embeddable
public class UserBirthday {

    private Date value;

    protected UserBirthday() {
    }

    public UserBirthday(Date value) {
        this.value = validate(value);
    }

    private static Date validate(Date value) {
        LocalDate birthDate = value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        Period age = Period.between(birthDate, today);
        if (age.getYears() >= 18) {
            return value;
        } else {
            throw new BaseException("No cumple", List.of("El usuario no es mayor de edad"));
        }
    }

    public Date getValue() {
        return value;
    }
}

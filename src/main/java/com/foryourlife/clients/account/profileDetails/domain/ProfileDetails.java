package com.foryourlife.clients.account.profileDetails.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.shared.domain.AggregateRoot;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "profile_details")
public class ProfileDetails extends AggregateRoot {
    @Id
    private String id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "birthday"))
    @JsonProperty("birthday")
    private UserBirthday birthday;

    private String address;
    private String occupation;
    private String gender;
    private String civilStatus;
    private String dni;
    private String city;


    protected ProfileDetails() {
    }

    public ProfileDetails(String id, Date birthday, String address, String occupation, String gender, String civilStatus, String dni, String city) {
        this.id = id;
        this.birthday = new UserBirthday(birthday);
        this.address = address;
        this.occupation = occupation;
        this.gender = gender;
        this.civilStatus = civilStatus;
        this.dni = dni;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    @JsonProperty("birthday")
    public Date getBirthday() {
        return birthday.getValue();
    }

    public String getAddress() {
        return address;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getGender() {
        return gender;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public String getDni() {
        return dni;
    }

    public String getCity() {
        return city;
    }

    @JsonProperty(value = "age", access = JsonProperty.Access.READ_ONLY)
    public Integer getAge() {
        if (birthday == null || birthday.getValue() == null) {
            return null;
        }

        LocalDate birthDate = birthday.getValue()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return Period.between(birthDate, LocalDate.now()).getYears();
    }

}

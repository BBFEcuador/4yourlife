package com.foryourlife.clients.account.profileDetails.domain;


import com.foryourlife.shared.domain.AggregateRoot;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "profile_details")
public class ProfileDetails extends AggregateRoot {
    @Id
    private String id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "birthday"))
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

    public UserBirthday getBirthday() {
        return birthday;
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
}

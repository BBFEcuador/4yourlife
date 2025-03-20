package com.foryourlife.clients.account.profileDetails.infrastructure;

import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.UUID;

public class ProfileDetailRequest {

    public String id;
    @Past(message = "fecha debe ser en pasado")
    @NotNull(message = "fecha es requerida")
    public Date birthday;
    @NotNull(message = "direccion es requerida")
    public String address;
    @NotNull(message = "ocupacion es requerida")
    public String occupation;
    @NotNull(message = "genero es requerido")
    @Pattern(regexp = "^[MH]$", message = "genero debe ser M o H")
    @Length(min = 1, max = 1)
    public String gender;
    @NotNull(message = "estatus civil")
    public String civilStatus;
    @NotNull(message = "documento de identidad es requerido")
    public String dni;
    @NotNull(message = "ciudad de residencia es requerida")
    public String city;

    public ProfileDetails toDomain(){
        return new ProfileDetails(id != null ? id : UUID.randomUUID().toString(),birthday,address,occupation,gender,civilStatus,dni,city);
    }

    public ProfileDetailRequest(String id, Date birthday, String address, String occupation, String gender, String civilStatus, String dni, String city) {
        this.id = id;
        this.birthday = birthday;
        this.address = address;
        this.occupation = occupation;
        this.gender = gender;
        this.civilStatus = civilStatus;
        this.dni = dni;
        this.city = city;
    }
}

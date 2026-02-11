package com.foryourlife.clients.account.participant.infrastructure.httpControllers;

import com.foryourlife.clients.account.contact.infrastructure.httpControllers.SaveContactRequest;
import com.foryourlife.clients.account.invoiceData.infrastructure.httpControllers.InvoiceDataRequest;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.profileDetails.infrastructure.ProfileDetailRequest;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SaveParticipantRequest {
    public String id;
    @NotNull
    @Email
    @NotBlank(message = "The User/Email field is required")
    public String email;
    @NotNull
    @NotBlank(message = "The password field is required")
    public String password;
    @NotNull
    @NotBlank(message = "The token field is required")
    public String token;
    @NotNull
    @NotBlank(message = "The name1 field is required")
    public String name1;

    @NotNull
    @NotBlank(message = "The nickname field is required")
    public String nickname;

    public String name2;
    @NotNull
    @NotBlank(message = "The lastname1 field is required")
    public String lastname1;

    public String lastname2;
    @NotNull
    @NotBlank(message = "The phone field is required")
    public String phone;
    @NotNull
    @Valid
    public ProfileDetailRequest profile;
    @NotNull
    @Valid
    public MedicalRecordSaveRequest medicalRecord;
    @NotNull(message = "The termsAndConditions field is required")
    public Boolean termsAndConditions;

    @NotNull
    @Valid
    public SaveContactRequest contact;

    @Valid
    public InvoiceDataRequest dataInvoice;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public String getLastname1() {
        return lastname1;
    }

    public String getLastname2() {
        return lastname2;
    }

    public String getPhone() {
        return phone;
    }

    public ProfileDetailRequest getProfile() {
        return profile;
    }

    public InvoiceDataRequest getDataInvoice() {
        return dataInvoice;
    }

    public MedicalRecordSaveRequest getMedicalRecord() {
        return medicalRecord;
    }



    public Participant toDomain() {
        var newId = id != null ? id : UUID.randomUUID().toString();
        if (!termsAndConditions){
            throw new IllegalArgumentException("Los terminos y condiciones deben ser aceptados.");
        }
        return Participant.create(
                newId,
                new User(
                        UUID.randomUUID().toString(),
                        email.toLowerCase().trim(),
                        password.trim(),
                        name1,
                        nickname,
                        name2,
                        lastname1,
                        lastname2,
                        Stream.of(name1, name2, lastname1, lastname2)
                                .filter(s -> s != null && !s.trim().isEmpty())
                                .collect(Collectors.joining(" ")),
                        phone,
                        List.of(new UserEntities(newId, UserType.PARTICIPANT.toString()))
                ),
                null,
                profile.toDomain(),
                token,
                false,
                false,
                null
        );
    }
}

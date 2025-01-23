package com.foryourlife.clients.account.profileDetails.domain;

import net.datafaker.Faker;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileDetailsMother {
    public static ProfileDetails random() {
        var faker = new Faker();
        return new ProfileDetails(
                UUID.randomUUID().toString(),
                new Date(105, 0, 17),
                faker.address().fullAddress(),
                faker.job().position(),
                "M",
                "asd",
                "1754568637",
                faker.address().city()
        );
    }
}
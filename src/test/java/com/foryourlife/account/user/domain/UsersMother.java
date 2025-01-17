package com.foryourlife.account.user.domain;

import net.datafaker.Faker;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UsersMother {

    public static Users random() {
        var faker = new Faker();
        return Users.create(
                UUID.randomUUID().toString(),
                faker.internet().emailAddress(),
                faker.internet().password(),
                faker.name().name(),
                faker.phoneNumber().phoneNumber()
        );
    }

}
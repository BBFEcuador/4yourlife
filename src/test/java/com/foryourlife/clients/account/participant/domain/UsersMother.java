package com.foryourlife.clients.account.participant.domain;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetailsMother;
import com.foryourlife.shared.domain.level.CourseLevel;
import net.datafaker.Faker;

import java.util.UUID;

public class UsersMother {
    public static Participant random(){
        var faker = new Faker();
        return Participant.create(
                UUID.randomUUID().toString(),
                faker.internet().emailAddress(),
                faker.internet().password(),
                faker.name().name(),
                faker.phoneNumber().phoneNumber(),
                ParticipantLevel.create("55c3da1c-b516-4a55-9fdd-21317ee6e4c0","ROLE_INIT",true),
                ProfileDetailsMother.random()
        );
    }
}
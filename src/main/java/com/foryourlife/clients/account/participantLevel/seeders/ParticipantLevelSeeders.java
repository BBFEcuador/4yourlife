package com.foryourlife.clients.account.participantLevel.seeders;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ParticipantLevelSeeders {

    private final ParticipantLevelRepository _roleRepository;

    public ParticipantLevelSeeders(ParticipantLevelRepository _roleRepository) {
        this._roleRepository = _roleRepository;
    }

    @Bean
    CommandLineRunner initRoles(){
        return args ->{
            List<ParticipantLevel> roles = Arrays.asList(
                    ParticipantLevel.create("6642e863-7f6f-40a3-80e2-934388ade735","ROLE_INIT",true, CourseLevel.INIT),
                    ParticipantLevel.create("3024c8f1-d603-47fc-8369-0e90cd2e703e","ROLE_FOCUS",false, CourseLevel.FOCUS),
                    ParticipantLevel.create("55c3da1c-b516-4a55-9fdd-21317ee6e4c0","ROLE_YOUR",false, CourseLevel.YOUR),
                    ParticipantLevel.create("5b2da953-9791-47e6-a5b8-3442b52b8ebc","ROLE_LIFE",false, CourseLevel.LIFE),
                    ParticipantLevel.create("c762743d-2fea-4c7f-9d4b-3a0715039c4b","ROLE_MASTER_LIFE",false, CourseLevel.MASTER_LIFE)
            );
            this._roleRepository.saveAll(roles);
        };
    }
}

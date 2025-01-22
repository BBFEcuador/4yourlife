package com.foryourlife.clients.account.participantLevel.seeders;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
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
                    ParticipantLevel.create("55c3da1c-b516-4a55-9fdd-21317ee6e4c0","ROLE_INIT",true),
                    ParticipantLevel.create("3024c8f1-d603-47fc-8369-0e90cd2e703e","ROLE_FOCUS",false),
                    ParticipantLevel.create("3024c8f1-d603-47fc-8369-0e90cd2e703e","ROLE_YOUR",false),
                    ParticipantLevel.create("3024c8f1-d603-47fc-8369-0e90cd2e703e","ROLE_LIFE",false)
            );
            this._roleRepository.saveAll(roles);
        };
    }
}

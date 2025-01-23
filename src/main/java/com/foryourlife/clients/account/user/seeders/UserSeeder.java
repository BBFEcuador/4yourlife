package com.foryourlife.clients.account.user.seeders;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.admin.auth.domain.AdminRole;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.clients.account.user.domain.UserRepository;
import com.foryourlife.clients.account.user.domain.Users;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserSeeder {
    private final UserRepository _userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ParticipantLevelRepository _participantLevelRepository;

    public UserSeeder(UserRepository _userRepository, PasswordEncoder passwordEncoder, ParticipantLevelRepository participantLevelRepository) {
        this._userRepository = _userRepository;
        this.passwordEncoder = passwordEncoder;
        _participantLevelRepository = participantLevelRepository;
    }

    @Bean
    CommandLineRunner initUser() {
        return args -> {
            ParticipantLevel domainRole = _participantLevelRepository.findById("55c3da1c-b516-4a55-9fdd-21317ee6e4c0")
                    .map(role -> ParticipantLevel.create(role.getId(), role.getRoleName(), role.getStarted()))
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            _userRepository.save(Users.create("84e0c1f5-d3e7-4a9f-83e3-83c0a40b9212", "testemail@fylbbf.com", passwordEncoder.encode("lowpassword"), "Jhon Doe", "0989999995", domainRole));
        };
    }
}

package com.foryourlife.admin.auth.seeders;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.admin.auth.domain.AdminRole;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.campus.domain.CampusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;

@Configuration
public class AdminSeeder {
    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final CampusRepository campusRepository;

    public AdminSeeder(AdminRepository repository, PasswordEncoder passwordEncoder, CampusRepository campusRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.campusRepository = campusRepository;
    }

    @Bean
    CommandLineRunner initAdmin() {
        return args -> {
            campusRepository.save(Campus.create("61d88b2a-a22e-4cb0-8e43-e036483039d6", "Ecuador", "Quito", "Av. Lo que sea", "094456123"));
            Admin admin = new Admin(
                    "3936ae5e-0cc1-4375-abc7-520d16999110",
                    "Diego FYL",
                    "diegofyl@admin.com",
                    passwordEncoder.encode("FocusYourLife2025--"),
                    new AdminRole("f4dddf05-8fec-4551-8d93-d6309c17c206", "Gerente", "ROLE_ADMIN"),
                    campusRepository.findById("61d88b2a-a22e-4cb0-8e43-e036483039d6").stream().collect(Collectors.toSet()),
                    true
            );


            repository.save(admin);
        };
    }
}

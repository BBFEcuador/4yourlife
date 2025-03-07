package com.foryourlife.admin.auth.seeders;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.admin.auth.domain.AdminRole;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.campus.domain.CampusRepository;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class AdminSeeder {
    private final AdminRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CampusRepository campusRepository;

    public AdminSeeder(AdminRepository repository, UserRepository userRepository, PasswordEncoder passwordEncoder, CampusRepository campusRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.campusRepository = campusRepository;
    }

    @Bean
    CommandLineRunner initAdmin() {
        return args -> {
            campusRepository.save(Campus.create("61d88b2a-a22e-4cb0-8e43-e036483039d6", "Ecuador", "Quito", "Av. Lo que sea", "094456123"));
            var user = new User("72efe963-52b5-439a-ada2-3ea7b0258b89", "diegofyl@admin.com", passwordEncoder.encode("FocusYourLife2025--"), "Diego FYL", "0999999999", List.of(new UserEntities("3936ae5e-0cc1-4375-abc7-520d16999110", "Admin")));
            userRepository.save(user);
            Admin admin = new Admin(
                    "3936ae5e-0cc1-4375-abc7-520d16999110",
                    user,
                    new AdminRole("f4dddf05-8fec-4551-8d93-d6309c17c206", "Gerente", "ROLE_ADMIN"),
                    new HashSet<>(campusRepository.getAll()),
                    true
            );
            repository.save(admin);
        };
    }
}

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
            campusRepository.save(Campus.create("61d88b2a-a22e-4cb0-8e43-e036483039d6", "Ecuador", "Quito", "De los Cedros OE1-13 y Real Audiencia", "094456123"));
            campusRepository.save(Campus.create("01a680a4-3bf6-4caf-98b8-f3d5c7a8811b", "Ecuador", "Guayaquil", "Alborada etapa 14 manzana 5", "094456123"));
            campusRepository.save(Campus.create("a35d480e-f17a-4b5c-887e-2ba9ddd3b696", "Ecuador", "Cuenca", "Por definir", "094456123"));
            repository.save(new Admin(
                    "3936ae5e-0cc1-4375-abc7-520d16999110",
                    new User("72efe963-52b5-439a-ada2-3ea7b0258b89", "diegofyl@admin.com", passwordEncoder.encode("FocusYourLife2025--"), "Diego FYL", "0999999999", List.of(new UserEntities("3936ae5e-0cc1-4375-abc7-520d16999110", "ADMIN"))),
                    new AdminRole("f4dddf05-8fec-4551-8d93-d6309c17c206", "Gerente", "ROLE_ADMIN"),
                    new HashSet<>(campusRepository.getAll()),
                    true
            ));
            repository.save(new Admin(
                    "1a34e4df-33fa-4078-9acd-df7d04dde5f4",
                    new User("93030dda-f43f-44bb-a500-991cda675aa7", "jairo@admin.com", passwordEncoder.encode("FocusYourLife2025--"), "Jairo FYL", "0999999999", List.of(new UserEntities("3936ae5e-0cc1-4375-abc7-520d16999110", "ADMIN"))),
                    new AdminRole("f4dddf05-8fec-4551-8d93-d6309c17c206", "Gerente", "ROLE_ADMIN"),
                    new HashSet<>(campusRepository.getAll()),
                    true
            ));
            repository.save(new Admin(
                    "b85430d1-e9fc-4c8d-8dd0-6c483511f0b8",
                    new User("4f158ff7-3584-4c1b-abce-d1cbee0ced26", "katy@admin.com", passwordEncoder.encode("FocusYourLife2025--"), "Katy FYL", "0999999999", List.of(new UserEntities("3936ae5e-0cc1-4375-abc7-520d16999110", "ADMIN"))),
                    new AdminRole("f4dddf05-8fec-4551-8d93-d6309c17c206", "Gerente", "ROLE_ADMIN"),
                    new HashSet<>(campusRepository.getAll()),
                    true
            ));
        };
    }
}

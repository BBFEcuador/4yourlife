package com.foryourlife.admin.auth.seeders;

import com.foryourlife.admin.auth.application.AdminCreateService;
import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.admin.auth.domain.AdminRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {
    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;
    public AdminSeeder(AdminRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initAdmin(){
        return args -> {
            Admin admin = new Admin(
                    "3936ae5e-0cc1-4375-abc7-520d16999110",
                    "Diego FYL",
                    "diegofyl@admin.com",
                    passwordEncoder.encode("FocusYourLife2025--"),
                    new AdminRole("f4dddf05-8fec-4551-8d93-d6309c17c206","Gerente","ROLE_ADMIN")
            );
            repository.save(admin);
        };
    }
}

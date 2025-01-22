package com.foryourlife.admin.auth.seeders;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.admin.auth.domain.AdminRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminSeeder {
    private final AdminRepository repository;
    public AdminSeeder(AdminRepository repository) {
        this.repository = repository;
    }

    @Bean
    CommandLineRunner initAdmin(){
        return args -> {
            Admin admin = new Admin(
                    "3936ae5e-0cc1-4375-abc7-520d16999110",
                    "Diegowowowowo",
                    "diegowo@admin.com",
                    "diegowo",
                    new AdminRole("f4dddf05-8fec-4551-8d93-d6309c17c206","Gerente","ROLE_ADMIN")
            );
        };
    }
}

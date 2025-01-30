package com.foryourlife.admin.auth.seeders;

import com.foryourlife.admin.auth.domain.AdminRole;
import com.foryourlife.admin.auth.domain.AdminRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AdminRoleSeeder {
    @Autowired
    private AdminRoleRepository adminRoleRepository;

    @Bean
    CommandLineRunner initAdminRoles(){
        return args ->{
            List<AdminRole> adminRoles = Arrays.asList(
                    AdminRole.create("617bfc68-2e44-4abd-b67f-781a2dca2493", "Operativo", "ROLE_OPERATOR"),
                    AdminRole.create("a39d4053-d591-4d3e-9b8b-a5fccbeb70c7", "Contador", "ROLE_ACCOUNTANT")

            );
            adminRoleRepository.saveAll(adminRoles);
        };
    }
}

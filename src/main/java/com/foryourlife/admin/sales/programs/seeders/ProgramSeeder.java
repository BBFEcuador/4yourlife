package com.foryourlife.admin.sales.programs.seeders;

import com.foryourlife.admin.sales.programs.domain.Program;
import com.foryourlife.admin.sales.programs.domain.ProgramRepository;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ProgramSeeder {
    private final ProgramRepository repository;

    public ProgramSeeder(ProgramRepository repository) {
        this.repository = repository;
    }

    @Bean
    CommandLineRunner initPrograms(){
        return args -> {
            List<Program> programs = Arrays.asList(
                    Program.create("55c3da1c-b516-4a55-9fdd-21317ee6e4c0","Focus", CourseLevel.FOCUS),
                    Program.create("bff32809-d719-4dfd-90b0-6f7a0f63e2fe","Your",CourseLevel.YOUR),
                    Program.create("3024c8f1-d603-47fc-8369-0e90cd2e703e","Life",CourseLevel.LIFE)
            );
            this.repository.saveAll(programs);
        };
    }
}

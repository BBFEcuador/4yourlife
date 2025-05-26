package com.foryourlife.admin.sales.programs.seeders;

import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.admin.sales.programs.domain.Program;
import com.foryourlife.admin.sales.programs.domain.ProgramRepository;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Configuration
public class ProgramSeeder {
    private final ProgramRepository programRepository;
    private final ProductRepository productRepository;

    public ProgramSeeder(ProgramRepository repository, ProgramRepository programRepository, ProductRepository productRepository) {
        this.programRepository = programRepository;
        this.productRepository = productRepository;
    }

    @Bean
    CommandLineRunner initPrograms(){
        return args -> {
            List<Program> programs = Arrays.asList(
                    Program.create("55c3da1c-b516-4a55-9fdd-21317ee6e4c0","Focus", CourseLevel.FOCUS),
                    Program.create("bff32809-d719-4dfd-90b0-6f7a0f63e2fe","Your",CourseLevel.YOUR),
                    Program.create("3024c8f1-d603-47fc-8369-0e90cd2e703e","Life",CourseLevel.LIFE)
            );
            this.programRepository.saveAll(programs);

            this.productRepository.save(Product.create("1a9f5420-a08d-4b66-99ed-616a7a50864a", "Focus + Your","elweso-code",700.5,"DOLAR",true,"Focus + Your",null,List.of(programs.get(0), programs.get(1))));
        };
    }
}

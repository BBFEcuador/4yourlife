package com.foryourlife.admin.sales.rules.seeder;

import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.admin.sales.rules.domain.Rule;
import com.foryourlife.admin.sales.rules.domain.RuleRepository;
import com.foryourlife.admin.sales.rules.domain.RuleType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleSeeder {
    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Bean
    CommandLineRunner initRules() {
        return args -> {
        };

    }



}

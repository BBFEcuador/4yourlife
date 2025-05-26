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
            seedRules();
        };

    }

    public void seedRules() {
        Product product = productRepository.findById("1a9f5420-a08d-4b66-99ed-616a7a50864a")
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        List<Rule> rules = List.of(
                Rule.create("978b5d39-97a8-4c73-a0c6-c609b2645928", "PARA REGALO", 0.00, true, product, RuleType.GIFT),
                Rule.create("01c535d9-6e01-40e9-b5a3-b5842b05cfb7", "PARA PARTICIPANTES EN DU FIN DE SEMANA DE LIFE", 655.00, true, product, RuleType.WEEKEND),
                Rule.create("4462531b-4314-4e6a-bce0-0497fb7a67e1", "PARA MASTER LIFE EN SU FIN DE SEMANA DE LIFE", 561.00, true, product, RuleType.MASTER),
                Rule.create("2737d6ba-7825-4210-9fa4-5e585c8944e0", "PRECIO NORMAL", 910.00, true, product, RuleType.STANDARD)
        );

        rules.forEach(rule ->
                ruleRepository.save(rule)
        );
    }

}

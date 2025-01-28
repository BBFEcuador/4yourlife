package com.foryourlife.admin.product.seeders;

import com.foryourlife.admin.product.domain.Product;
import com.foryourlife.admin.product.domain.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Array;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Configuration
public class ProductSeeder {

    private final ProductRepository repository;

    public ProductSeeder(ProductRepository repository) {
        this.repository = repository;
    }

    @Bean
    CommandLineRunner initProducts() {
        return args -> {
            List<Product> products = Arrays.asList(
                    Product.create("1qdGvvPAkTLxXbN8", "ENTRENAMIENTO BECA", "ENTRE-BECA", true, "", 0.0, 15, "SER", "nRMdRgrN2ig9Zel6", 0.0, 0.0, 0.0, 0.0, Instant.now(), Instant.now()),
                    Product.create("gQbWn9AxYHy7oa6w", "FOCUS PRESENCIAL", "FOCUS-PRES.", true, "", 241.74, 15, "SER", "nRMdRgrN2ig9Zel6", 192.17, 313.04, 0.0, 0.0, Instant.now(), Instant.now()),
                    Product.create("KVeZ49KyBcoZBb8P", "PROCESO FOCUS+YOUR", "PROCESO FOCUS+YOUR", true, "", 569.57, 15, "SER", "nRMdRgrN2ig9Zel6", 487.83, 0.0, 0.0, 0.0, Instant.now(), Instant.now()),
                    Product.create("LmavolwMQCymVb3N", "ENTRENAMIENTO COMPLETO PRESENCIAL", "ENTRE-PRESEN", true, "", 881.74, 15, "SER", "nRMdRgrN2ig9Zel6", 755.65, 937.50, 0.0, 0.0, Instant.now(), Instant.now()),
                    Product.create("Pnazmw840hmlVeO2", "YOUR PRESENCIAL", "YOUR-PRESENC.", true, "", 368.70, 15, "SER", "nRMdRgrN2ig9Zel6", 429.57, 491.30, 0.0, 0.0, Instant.now(), Instant.now()),
                    Product.create("y7aAnYLo8u1yqbgZ", "LIFE PRESENCIAL", "LIFE-PRESENC.", true, "", 377.39, 15, "SER", "nRMdRgrN2ig9Zel6", 428.70, 469.57, 0.0, 0.0, Instant.now(), Instant.now()),
                    Product.create("y7aAP9xMKu1yqegZ", "YOUR + LIFE PRESENCIAL", "Your + Life Presencial.", true, "", 703.91, 15, "SER", "nRMdRgrN2ig9Zel6", 530.00, 937.50, 0.0, 0.0, Instant.now(), Instant.now())
            );
            this.repository.saveAll(products);
        };
    }
}

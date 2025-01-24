package com.foryourlife.admin.product.seeders;

import com.foryourlife.admin.product.domain.Product;
import com.foryourlife.admin.product.domain.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

public class ProductSeeder {

    private final ProductRepository repository;

    public ProductSeeder(ProductRepository repository) {
        this.repository = repository;
    }

    @Bean
    CommandLineRunner initProducts() {
        return args -> {
            List<Product> products = Arrays.asList(
//                    Product.create("1qdGvvPAkTLxXbN8", "ENTRENAMIENTO BECA", "ENTRE-BECA", true, null,)
            );
        };
    }
}

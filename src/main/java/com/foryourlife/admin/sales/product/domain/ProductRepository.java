package com.foryourlife.admin.sales.product.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(Product product);

    Optional<Product> findById(String id);

    void deleteById(String id);

    List<Product> findAll();

    void saveAll(List<Product> products);
}

package com.foryourlife.admin.sales.product.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(Product product);

    Optional<Product> findById(String id);

    void deleteById(String id);

    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);

    void saveAll(List<Product> products);
}

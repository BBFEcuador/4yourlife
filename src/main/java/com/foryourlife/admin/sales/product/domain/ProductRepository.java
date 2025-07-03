package com.foryourlife.admin.sales.product.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(String id);
    Optional<Product> findByContificoId(String contificoId);
    Page<Product> findAll(Pageable pageable, Criteria criteria);
    Page<Product> findAll(Pageable pageable);
    Page<Product> findAllAvailable(Pageable pageable);

    void saveAll(List<Product> products);
}

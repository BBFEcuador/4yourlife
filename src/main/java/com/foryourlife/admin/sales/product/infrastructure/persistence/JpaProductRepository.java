package com.foryourlife.admin.sales.product.infrastructure.persistence;

import com.foryourlife.admin.sales.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, String> {
}

package com.foryourlife.admin.sales.product.infrastructure.persistence;

import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    Page<Product> findAllByIsActiveTrue(Pageable pageable);
    Optional<Product> findByContificoId(String contificoId);
}

package com.foryourlife.admin.sales.discounts.infrastructure.persistense;

import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPAProductDiscountRepository extends JpaRepository<ProductDiscount, String>, JpaSpecificationExecutor<ProductDiscount> {
}

package com.foryourlife.admin.sales.discounts.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductDiscountRepository {

    void save(ProductDiscount productDiscount);

    List<ProductDiscount> findAll();

    Page<ProductDiscount> findAll(Pageable pageable, Criteria criteria);

    Page<ProductDiscount> findAvailable(Pageable pageable, Criteria criteria);

    Optional<ProductDiscount> findById(String id);
}

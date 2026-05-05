package com.foryourlife.admin.sales.discounts.applications;

import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import com.foryourlife.admin.sales.discounts.domain.ProductDiscountRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDiscountQueryService {

    private final ProductDiscountRepository repository;

    public ProductDiscountQueryService(ProductDiscountRepository repository) {
        this.repository = repository;
    }

    public List<ProductDiscount> findAll() {
        return repository.findAll();
    }

    public Page<ProductDiscount> findAll(Pageable pageable, Criteria criteria) {
        return repository.findAll(pageable, criteria);
    }

    public Page<ProductDiscount> findAvailable(Pageable pageable, Criteria criteria) {
        return repository.findAvailable(pageable, criteria);
    }
}

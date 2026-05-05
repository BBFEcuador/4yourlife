package com.foryourlife.admin.sales.discounts.infrastructure.persistense;

import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import com.foryourlife.admin.sales.discounts.domain.ProductDiscountRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDiscountRepositoryImpl implements ProductDiscountRepository {

    private final JPAProductDiscountRepository repository;
    private final JPACriteriaConverter<ProductDiscount> criteriaConverter;

    public ProductDiscountRepositoryImpl(JPAProductDiscountRepository jpaProductDiscountRepository, JPACriteriaConverter<ProductDiscount> criteriaConverter) {
        this.repository = jpaProductDiscountRepository;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public void save(ProductDiscount productDiscount) {
        repository.save(productDiscount);
    }

    @Override
    public List<ProductDiscount> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<ProductDiscount> findAll(Pageable pageable, Criteria criteria) {
        return repository.findAll(
                criteriaConverter.getJpaSpecifications(criteria),
                pageable
        );
    }

    @Override
    public Page<ProductDiscount> findAvailable(Pageable pageable, Criteria criteria) {

        Specification<ProductDiscount> spec = Specification
                .where(criteriaConverter.getJpaSpecifications(criteria))
                .and((root, query, cb) -> cb.isTrue(root.get("isActive")));

        return repository.findAll(spec, pageable);
    }

    @Override
    public Optional<ProductDiscount> findById(String id) {
        return repository.findById(id);
    }
}

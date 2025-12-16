package com.foryourlife.admin.sales.product.infrastructure.persistence;

import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository impl;
    private final JPACriteriaConverter<Product> criteriaConverter;

    public ProductRepositoryImpl(JpaProductRepository impl, JPACriteriaConverter<Product> criteriaConverter) {
        this.impl = impl;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public Product save(Product product) {
        return this.impl.save(product);
    }

    @Override
    public Optional<Product> findById(String id) {
        return this.impl.findById(id);
    }

    @Override
    public Page<Product> findAll(Pageable pageable, Criteria criteria) {
        return this.impl.findAll(criteriaConverter.getJpaSpecifications(criteria), pageable);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return this.impl.findAll(pageable);
    }

    @Override
    public Page<Product> findAllAvailable(Pageable pageable) {
        return impl.findAllByIsActiveTrue(pageable);
    }

    @Override
    public void saveAll(List<Product> products) {
        this.impl.saveAll(products);
    }

    @Override
    public Optional<Product> findByContificoId(String contificoId) {
        return this.impl.findByContificoId(contificoId);
    }

    @Override
    public List<Product> findAllByCampusId(String campusId) {
        return impl.findAllByCampus_Id(campusId);
    }
}

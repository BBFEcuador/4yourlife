package com.foryourlife.admin.product.infrastructure.persistence;

import com.foryourlife.admin.product.domain.Product;
import com.foryourlife.admin.product.domain.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository impl;

    public ProductRepositoryImpl(JpaProductRepository impl) {
        this.impl = impl;
    }

    @Override
    public void save(Product product) {
        this.impl.save(product);
    }

    @Override
    public Optional<Product> findById(String id) {
        return this.impl.findById(id);
    }

    @Override
    public void deleteById(String id) {
        this.impl.deleteById(id);
    }

    @Override
    public List<Product> findAll() {
        return this.impl.findAll();
    }
}

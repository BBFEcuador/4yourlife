package com.foryourlife.admin.product.application;

import com.foryourlife.admin.product.domain.Product;
import com.foryourlife.admin.product.domain.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductFinderService {

    private final ProductRepository repository;

    public ProductFinderService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(String id) {
        return repository.findById(id).orElse(null);
    }
}

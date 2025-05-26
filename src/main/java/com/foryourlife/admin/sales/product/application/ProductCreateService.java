package com.foryourlife.admin.sales.product.application;

import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCreateService {

    private final ProductRepository repository;

    public ProductCreateService(ProductRepository repository) {
        this.repository = repository;
    }

    public void saveProduct(Product product) {
        repository.save(product);
    }

    public void disableProductById(String id) {
        var product = repository.findById(id).orElseThrow(() -> {;
            return new IllegalArgumentException("Product doesn't exist with id: " + id);
        });
        product.setActive(!product.getActive());
        repository.save(product);
    }

    public void updateProduct(Product product) {
        repository.findById(product.getId()).orElseThrow(()->{
            return new IllegalArgumentException("Product doesn't exist with id: " + product.getId());
        });
        repository.save(product);
    }
}

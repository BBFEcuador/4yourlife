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

    public void deleteProductById(String id) {
        repository.deleteById(id);
    }

    public void updateProduct(Product product) {
        repository.save(product);
    }

    public Optional<Product> findProductById(String id) {
        return repository.findById(id);
    }

    public List<Product> findAllProducts() {
        return repository.findAll();
    }
}

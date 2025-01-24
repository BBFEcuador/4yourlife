package com.foryourlife.admin.product.infrastructure.httpControllers;

import com.foryourlife.admin.product.application.ProductCreateService;
import com.foryourlife.admin.product.application.ProductFinderService;
import com.foryourlife.admin.product.domain.Product;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductCreateService createService;

    @Autowired
    private ProductFinderService finderService;

    @GetMapping("/")
    public ResponseEntity<?> getAllProducts() {
      return new ResponseEntity<>(finderService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> updateProduct(Product product) {
        createService.saveProduct(product);
        return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
      return new ResponseEntity<>(finderService.findById(id), HttpStatus.OK);
    }
}

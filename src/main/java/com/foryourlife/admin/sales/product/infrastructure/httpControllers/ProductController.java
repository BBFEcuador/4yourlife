package com.foryourlife.admin.sales.product.infrastructure.httpControllers;

import com.foryourlife.admin.sales.product.application.ProductCreateService;
import com.foryourlife.admin.sales.product.application.ProductFinderService;
import com.foryourlife.admin.sales.product.domain.Product;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @GetMapping("")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage
    ) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        return new ResponseEntity<>(finderService.findAll(p), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductRequest product) {
        createService.updateProduct(product.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> saveProduct(@RequestBody @Valid ProductRequest product) {
        createService.saveProduct(product.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
      return new ResponseEntity<>(finderService.findById(id), HttpStatus.OK);
    }
}

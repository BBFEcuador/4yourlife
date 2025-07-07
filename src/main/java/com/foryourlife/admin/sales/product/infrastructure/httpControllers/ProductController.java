package com.foryourlife.admin.sales.product.infrastructure.httpControllers;

import com.foryourlife.admin.sales.product.application.ProductCreateService;
import com.foryourlife.admin.sales.product.application.ProductFinderService;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductCreateService createService;

    @Autowired
    private ProductFinderService finderService;

    @GetMapping("")
    public ResponseEntity<?> getAllProducts(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "perPage", defaultValue = "10") int perPage, @RequestParam(value = "search", defaultValue = "") String search) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());

        if (search.isEmpty()) {
            return new ResponseEntity<>(finderService.findAll(p), HttpStatus.OK);
        } else {
            Criteria criteria = new Criteria(List.of(new Filter("name", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("code", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("description", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR)), Optional.empty(), Optional.empty());
            return new ResponseEntity<>(finderService.findAll(p, criteria), HttpStatus.OK);
        }
    }

    @GetMapping("/campus/{campusId}")
    public ResponseEntity<?> getAllProductsByCampus(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "perPage", defaultValue = "10") int perPage, @RequestParam(value = "search", defaultValue = "") String search, @PathVariable String campusId) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());

        if (search.isEmpty()) {
            Criteria criteria = new Criteria(List.of(new Filter("id", campusId, "campus", Filter.Operation.LIKE, Filter.LogicalOperator.OR)), Optional.empty(), Optional.empty());
            return new ResponseEntity<>(finderService.findAll(p, criteria), HttpStatus.OK);
        } else {
            Criteria criteria = new Criteria(List.of(new Filter("name", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("code", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("description", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("id", campusId, "campus", Filter.Operation.LIKE, Filter.LogicalOperator.OR)), Optional.empty(), Optional.empty());
            return new ResponseEntity<>(finderService.findAll(p, criteria), HttpStatus.OK);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAllProducts(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "perPage", defaultValue = "10") int perPage) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        return new ResponseEntity<>(finderService.findAllAvailable(p), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductRequest product) {
        createService.updateProduct(product.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> saveProduct(@RequestBody @Valid ProductRequest product) {
        createService.saveProduct(product.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> disableProduct(@PathVariable String id) {
        createService.disableProductById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        return new ResponseEntity<>(finderService.findById(id), HttpStatus.OK);
    }

    @PostMapping("sync")
    public ResponseEntity<?> syncProducts(@RequestParam String campusId) {
        createService.syncProducts(campusId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

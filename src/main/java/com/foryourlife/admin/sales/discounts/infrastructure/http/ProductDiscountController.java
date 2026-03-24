package com.foryourlife.admin.sales.discounts.infrastructure.http;

import com.foryourlife.admin.sales.discounts.applications.ProductDiscountCommandService;
import com.foryourlife.admin.sales.discounts.applications.ProductDiscountQueryService;
import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product-discounts")
public class ProductDiscountController {

    private final ProductDiscountCommandService commandService;
    private final ProductDiscountQueryService queryService;

    public ProductDiscountController(ProductDiscountCommandService commandService, ProductDiscountQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping("/available")
    public ResponseEntity<List<ProductDiscount>> create(@Valid @RequestBody ProductDiscountRequest request) {
        List<ProductDiscount> productDiscounts = queryService.findAll();
        return ResponseEntity.ok(productDiscounts);
    }

    @PostMapping("")
    public ResponseEntity<Void> findAvailable(@Valid @RequestBody ProductDiscountRequest request) {
        commandService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/change-status")
    public ResponseEntity<Void> changeStatus(@Valid @RequestBody ChangeStatusRequest request) {
        commandService.changeStatus(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("")
    public ResponseEntity<Page<ProductDiscount>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "campusId", defaultValue = "") String campusId
    ) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        Criteria criteria = new Criteria(
                List.of(), Optional.empty(), Optional.empty()
        );

        if (!search.isEmpty()) {
            criteria.filters =
                    List.of(
                            new Filter(
                                    "name",
                                    search,
                                    null,
                                    Filter.Operation.LIKE,
                                    Filter.LogicalOperator.OR
                            )
                    );
        }
        Page<ProductDiscount> productDiscounts = queryService.findAll(p, criteria);
        return ResponseEntity.ok(productDiscounts);
    }
}

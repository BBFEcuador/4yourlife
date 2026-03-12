package com.foryourlife.admin.sales.payments.store.infrastructure.http;

import com.foryourlife.admin.sales.payments.store.application.StoreCommandService;
import com.foryourlife.admin.sales.payments.store.application.StoreQueryService;
import com.foryourlife.admin.sales.payments.store.domain.Store;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private StoreCommandService storeService;
    @Autowired
    private StoreQueryService storeQueryService;

    @PostMapping("")
    public ResponseEntity<?> createStore(@RequestParam String campusId) {
        storeService.syncStoresFromContifico(campusId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("campus/{campusId}")
    public ResponseEntity<?> getAllStoresByCampus(@PathVariable String campusId) {
        return new ResponseEntity<>(storeQueryService.getAllStoresByCampus(campusId), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getStoreById(@PathVariable String id) {
        return new ResponseEntity<>(storeQueryService.getStoreById(id), HttpStatus.OK);
    }

    @PostMapping("add-change")
    public ResponseEntity<?> addStore(@RequestBody @Valid StoreRequest store) {
        storeService.create(store);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Page<Store>> getAllStores(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "campusId", defaultValue = "") String campusId) {
        var pageable = PageRequest.of(page, perPage, Sort.by("number").descending());

        Criteria criteria = new Criteria(List.of(), Optional.empty(), Optional.empty());
        List<Filter> filters = new ArrayList<>();
        if (!search.isEmpty()) {
            filters.addAll(
                    List.of(
                            new Filter("address", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                            new Filter("number", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR)
                    )
            );
        }
        if (!campusId.isEmpty()) {
            filters.add(new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND));
        }
        criteria.filters = filters;

        return new ResponseEntity<>(storeQueryService.getAllStores(pageable, criteria), HttpStatus.OK);
    }
}

package com.foryourlife.admin.sales.payments.store.infrastructure.http;

import com.foryourlife.admin.sales.payments.store.application.StoreCommandService;
import com.foryourlife.admin.sales.payments.store.application.StoreQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getAllStoresByCampus(@RequestParam String campusId) {
        return new ResponseEntity<>(storeQueryService.getAllStoresByCampus(campusId), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getStoreById(@PathVariable String id) {
        return new ResponseEntity<>(storeQueryService.getStoreById(id), HttpStatus.OK);
    }
}

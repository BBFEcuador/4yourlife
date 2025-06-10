package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.httpControllers;

import com.foryourlife.admin.sales.payments.cashDrawer.application.CashDrawerCommandService;
import com.foryourlife.admin.sales.payments.cashDrawer.application.CashDrawerQueryService;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cash-drawer")
public class CashDrawerController {
    @Autowired
    private CashDrawerCommandService commandService;
    @Autowired
    private CashDrawerQueryService queryService;

    @GetMapping("")
    public ResponseEntity<?> getAllCashDrawers() {
        return ResponseEntity.ok(queryService.getAllCashDrawers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCashDrawerById(@PathVariable String id) {
        return ResponseEntity.ok(queryService.getCashDrawerById(id));
    }

    @PutMapping("/open/{id}")
    public ResponseEntity<?> openDrawer(@PathVariable String id) {
        commandService.openDrawer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/close/{id}")
    public ResponseEntity<?> closeDrawer(@PathVariable String id) {
        commandService.closeDrawer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/opened-by-user/{userId}")
    public ResponseEntity<?> getCashDrawersOpenedByUser(@PathVariable String userId) {
        return ResponseEntity.ok(queryService.getCashDrawersByOpenedByUserId(userId));
    }

    @GetMapping("/closed-by-user/{userId}")
    public ResponseEntity<?> getCashDrawersClosedByUser(@PathVariable String userId) {
        return ResponseEntity.ok(queryService.getCashDrawersByClosedByUserId(userId));
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCashDrawer(@RequestBody CashDrawer cashDrawer) {
        commandService.save(cashDrawer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

package com.foryourlife.admin.sales.payments.cashBox.infrastructure.http;

import com.foryourlife.admin.sales.payments.cashBox.application.CashBoxCommandService;
import com.foryourlife.admin.sales.payments.cashBox.application.CashBoxQueryService;
import com.foryourlife.admin.sales.payments.cashBox.domain.CashBox;
import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cash-box")
public class CashBoxController {
    @Autowired
    private CashBoxCommandService commandService;
    @Autowired
    private CashBoxQueryService queryService;

    @PostMapping("")
    public ResponseEntity<CashBox> saveCashDrawer(@RequestBody @Valid CashBoxRequest cashDrawer) {
        return new ResponseEntity<>(commandService.addCashBox(cashDrawer.toDomain()), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCashDrawerById(@PathVariable String id) {
        return new ResponseEntity<>(queryService.getCashBoxById(id), HttpStatus.OK);
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<?> getCashDrawerByNumber(@PathVariable String number) {
        return new ResponseEntity<>(queryService.getCashBoxByNumber(number), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCashDrawers() {
        return new ResponseEntity<>(queryService.getAllCashBox(), HttpStatus.OK);
    }

    @GetMapping("available")
    public ResponseEntity<?> getAvailableCashDrawers(@RequestParam(value = "campusId",defaultValue = "") String campusId) {
        if (!campusId.isEmpty()) {
            return new ResponseEntity<>(queryService.getCashBoxNotOpened(campusId), HttpStatus.OK);
        } else {
            throw new BaseException("Seleccione un campus para continuar", List.of(""));
        }
    }
}

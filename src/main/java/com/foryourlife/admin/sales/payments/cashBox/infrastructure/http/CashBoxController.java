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
    public ResponseEntity<?> saveCashBox(@RequestBody @Valid CashBoxRequest cashDrawer) {
        commandService.addCashBox(cashDrawer.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCashBoxById(@PathVariable String id) {
        return new ResponseEntity<>(queryService.getCashBoxById(id), HttpStatus.OK);
    }


    @GetMapping("")
    public ResponseEntity<?> getAllCashBoxes(@RequestParam(value = "campusId", defaultValue = "") String campusId) {
        if (!campusId.isEmpty()) {
            return new ResponseEntity<>(queryService.getAllCashBoxByCampus(campusId), HttpStatus.OK);
        } else {
            throw new BaseException("Seleccione un campus para continuar", List.of(""));
        }
    }

    @GetMapping("store/{storeId}")
    public ResponseEntity<List<CashBox>> getCashBoxByStoreId(@PathVariable String storeId) {
        return new ResponseEntity<>(queryService.getAllCashBoxByStoreId(storeId), HttpStatus.OK);
    }
}

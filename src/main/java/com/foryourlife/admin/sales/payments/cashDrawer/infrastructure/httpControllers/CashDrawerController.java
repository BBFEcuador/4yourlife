package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.httpControllers;

import com.foryourlife.admin.sales.payments.cashDrawer.application.CashDrawerCommandService;
import com.foryourlife.admin.sales.payments.cashDrawer.application.CashDrawerQueryService;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

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

    @GetMapping("cash-box/{id}")
    public ResponseEntity<?> getCashDrawersByCashBoxId(@PathVariable String id) {
        return ResponseEntity.ok(queryService.getCashDrawersByCashBoxId(id));
    }

    @PutMapping("/open/{cashBoxId}")
    public ResponseEntity<CashDrawer> openDrawer(@PathVariable String cashBoxId, @RequestParam String userId, @RequestParam Double openingBalance, @RequestParam String detail) {
        return new ResponseEntity<>(commandService.openDrawer(cashBoxId, userId, openingBalance, detail), HttpStatus.OK);
    }

    @PutMapping("/close/{id}")
    public ResponseEntity<?> closeDrawer(@PathVariable String id, @RequestParam String userId) {
        ByteArrayOutputStream pdfBytes = commandService.closeDrawer(id, userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "cash-drawer-report.pdf");

        return new ResponseEntity<>(pdfBytes.toByteArray(), headers, HttpStatus.OK);
    }
}

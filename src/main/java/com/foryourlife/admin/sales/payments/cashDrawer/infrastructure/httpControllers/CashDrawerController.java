package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.httpControllers;

import com.foryourlife.admin.sales.payments.cashDrawer.application.CashDrawerCommandService;
import com.foryourlife.admin.sales.payments.cashDrawer.application.CashDrawerQueryService;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
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

    @GetMapping("cash-box/{id}")
    public ResponseEntity<?> getCashDrawersByCashBoxId(@PathVariable String id) {
        return ResponseEntity.ok(queryService.getCashDrawersByCashBoxId(id));
    }

    @PutMapping("/open/{cashBoxId}")
    public ResponseEntity<CashDrawer> openDrawer(@PathVariable String cashBoxId, @RequestParam String userId, @RequestParam Double openingBalance, @RequestParam String detail) {
        return new ResponseEntity<>(commandService.openDrawer(cashBoxId, userId, openingBalance, detail), HttpStatus.OK);
    }

    @PutMapping("/close/{id}")
    public ResponseEntity<?> closeDrawer(@PathVariable String id, @RequestParam @Valid String userId) {
        ByteArrayOutputStream pdfBytes = commandService.closeDrawer(id, userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "cash-drawer-report.pdf");

        return new ResponseEntity<>(pdfBytes.toByteArray(), headers, HttpStatus.OK);
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<?> lockDrawer(@PathVariable String id, @RequestParam @Valid String pin) {
        commandService.lockCashDrawer(id, pin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/forget-pin/{id}")
    public ResponseEntity<?> forgetPin(@PathVariable String id) {
        commandService.forgetPin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("generate-report")
    public ResponseEntity<?> getDrawerReport(@RequestParam(value = "id", defaultValue = "") String id) {
        if (!id.isEmpty()) {
        ByteArrayOutputStream pdfBytes = commandService.getCloseReport(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "cash-drawer-report.pdf");

        return new ResponseEntity<>(pdfBytes.toByteArray(), headers, HttpStatus.OK);
        } else {
            throw new BaseException("Id esta vacío", List.of(""));
        }
    }
}

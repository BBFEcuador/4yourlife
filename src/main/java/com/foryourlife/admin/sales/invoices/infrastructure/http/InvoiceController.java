package com.foryourlife.admin.sales.invoices.infrastructure.http;

import com.foryourlife.admin.sales.invoices.application.CommandInvoiceService;
import com.foryourlife.admin.sales.invoices.application.QueryInvoiceService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final CommandInvoiceService commandInvoiceService;
    private final QueryInvoiceService queryInvoiceService;

    public InvoiceController(CommandInvoiceService commandInvoiceService, QueryInvoiceService queryInvoiceService) {
        this.commandInvoiceService = commandInvoiceService;
        this.queryInvoiceService = queryInvoiceService;
    }

    @PostMapping("")
    public ResponseEntity<?> saveInvoice(@RequestBody InvoiceRequest request) {
        commandInvoiceService.save(request.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<?> updateInvoice(@RequestBody InvoiceRequest request) {
        commandInvoiceService.update(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> findInvoices(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage
    ) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        return new ResponseEntity<>(queryInvoiceService.findAll(p), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> findInvoicesByUser(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @PathVariable String id
    ) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        return new ResponseEntity<>(queryInvoiceService.findByUserId(id, p), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findInvoiceById(
            @PathVariable String id
    ) {
        return new ResponseEntity<>(queryInvoiceService.findById(id), HttpStatus.OK);
    }
}

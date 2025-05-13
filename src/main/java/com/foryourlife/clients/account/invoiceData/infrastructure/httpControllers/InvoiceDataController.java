package com.foryourlife.clients.account.invoiceData.infrastructure.httpControllers;

import com.foryourlife.clients.account.invoiceData.application.InvoiceDataCommandService;
import com.foryourlife.clients.account.invoiceData.application.InvoiceDataQueryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice-data")
public class InvoiceDataController {
    @Autowired
    private InvoiceDataCommandService commandService;
    @Autowired
    private InvoiceDataQueryService queryService;

    @PostMapping("add")
    public ResponseEntity<?> addInvoiceData(@Valid @RequestBody InvoiceDataRequest request) {
        commandService.create(request.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<?> findInvoiceDataByUserId(@PathVariable String userId) {
        var invoiceData = queryService.getAllInvoiceDataByUserId(userId);
        return new ResponseEntity<>(invoiceData, HttpStatus.OK);
    }

}

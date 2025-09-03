package com.foryourlife.admin.sales.invoices.infrastructure.http;

import com.foryourlife.admin.sales.invoices.application.CommandInvoiceService;
import com.foryourlife.admin.sales.invoices.application.QueryInvoiceService;
import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("send")
    public ResponseEntity<?> sendInvoicesContifico(@RequestParam(value = "campusId", defaultValue = "") String campusId) {
        commandInvoiceService.resendToContifico(campusId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<?> updateInvoice(@RequestBody InvoiceRequest request) {
        commandInvoiceService.update(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> findInvoices(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "perPage", defaultValue = "10") int perPage, @RequestParam(value = "search", defaultValue = "") String search, @RequestParam(value = "campusId", defaultValue = "") String campusId) {
        var p = PageRequest.of(page, perPage, Sort.by("invoiceDate").descending());
        List<Filter> filters = new ArrayList<>();

        if (!search.isEmpty()) {
            filters.addAll(List.of(new Filter("fullName", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("address", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("document", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("phone", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("email", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR), new Filter("invoiceNumber", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR)));
        }

        if (!campusId.isEmpty()) {
            filters.add(new Filter("id", campusId, "payment.campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND));
        }

        var criteria = new Criteria(filters, Optional.empty(), Optional.empty());
        criteria.filters = filters;
        return new ResponseEntity<>(queryInvoiceService.findAll(p, criteria), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> findInvoicesByUser(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "perPage", defaultValue = "10") int perPage, @PathVariable String id) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        return new ResponseEntity<>(queryInvoiceService.findByUserId(id, p), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findInvoiceById(@PathVariable String id) {
        return new ResponseEntity<>(queryInvoiceService.findById(id), HttpStatus.OK);
    }

    @PostMapping("resend-payment-history")
    public ResponseEntity<?> sendPaymentHistory(@RequestParam(value = "paymentId") String paymentId) {
        commandInvoiceService.resendPaymentHistoryToContifico(paymentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

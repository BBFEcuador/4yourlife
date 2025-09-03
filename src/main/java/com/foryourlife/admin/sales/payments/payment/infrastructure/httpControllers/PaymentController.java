package com.foryourlife.admin.sales.payments.payment.infrastructure.httpControllers;

import com.foryourlife.admin.sales.payments.payment.application.CommandPaymentService;
import com.foryourlife.admin.sales.payments.payment.application.QueryPaymentService;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import jakarta.validation.Valid;
import javassist.bytecode.ByteArray;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final QueryPaymentService queryPaymentService;
    private final CommandPaymentService commandPaymentService;

    public PaymentController(QueryPaymentService queryPaymentService, CommandPaymentService commandPaymentService) {
        this.queryPaymentService = queryPaymentService;
        this.commandPaymentService = commandPaymentService;
    }

    @PostMapping("")
    public ResponseEntity<?> savePayment(@RequestBody @Valid PaymentRequest request) {
        return new ResponseEntity<>(commandPaymentService.save(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable String id) {
        return new ResponseEntity<>(queryPaymentService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/participant/{id}")
    public ResponseEntity<?> getPaymentsByParticipantId(@PathVariable String id, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "perPage", defaultValue = "10") int perPage) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        return new ResponseEntity<>(queryPaymentService.findByParticipantId(id, p), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPayments(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "perPage", defaultValue = "10") int perPage, @RequestParam(value = "search", defaultValue = "") String search, @RequestParam(value = "campusId", defaultValue = "") String campusId) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        Criteria criteria = new Criteria(List.of(), Optional.empty(), Optional.empty());
        List<Filter> filters = new ArrayList<>();
        if (!search.isEmpty()) {
            filters.addAll(
                    List.of(
                            new Filter("number", search, "cashDrawerDetail.cashDrawer.cashBox", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                            new Filter("fullName", search, "invoice", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                            new Filter("address", search, "invoice", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                            new Filter("document", search, "invoice", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                            new Filter("phone", search, "invoice", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                            new Filter("email", search, "invoice", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                            new Filter("invoiceNumber", search, "invoice", Filter.Operation.LIKE, Filter.LogicalOperator.OR)
                    )
            );
        }
        if (!campusId.isEmpty()) {
            filters.add(new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND));
        }
        criteria.filters = filters;
        return new ResponseEntity<>(queryPaymentService.findAll(p, criteria), HttpStatus.OK);
    }

    @PutMapping("add/payment-history/{paymentId}")
    public ResponseEntity<?> addPaymentHistory(@RequestBody @Valid PaymentHistoryRequest paymentHistory, @PathVariable String paymentId) {
        commandPaymentService.updatePaymentsHistory(paymentHistory.paymentHistory, paymentId, paymentHistory.cashDrawerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/change-status/{id}")
    public ResponseEntity<?> changePaymentStatus(@PathVariable String id, @RequestParam(name = "status", defaultValue = "") String status) {
        commandPaymentService.changeStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/generate-pdf/{paymentId}")
    public ResponseEntity<byte[]> generateInvoice(@PathVariable String paymentId) {
        ByteArrayOutputStream pdfBytes = commandPaymentService.generateInvoice(paymentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "payment.pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes.toByteArray());
    }
}

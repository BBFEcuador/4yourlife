package com.foryourlife.admin.sales.payments.payment.infraestructure.httpControllers;

import com.foryourlife.admin.sales.payments.payment.application.CommandPaymentService;
import com.foryourlife.admin.sales.payments.payment.application.QueryPaymentService;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        commandPaymentService.save(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<?> updatePayment(@RequestBody PaymentRequest request) {
        commandPaymentService.update(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPayments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage
    ) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        return new ResponseEntity<>(queryPaymentService.findAll(p), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable String id) {
        return new ResponseEntity<>(queryPaymentService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/participant/{id}")
    public ResponseEntity<?> getPaymentsByParticipantId(
            @PathVariable String id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        return new ResponseEntity<>(queryPaymentService.findByParticipantId(id, p), HttpStatus.OK);
    }

    @PutMapping("add/payment-history/{paymentId}")
    public ResponseEntity<?> addPaymentHistory(@RequestBody @Valid PaymentHistory paymentHistory, @PathVariable String paymentId) {
        commandPaymentService.updatePaymentsHistory(paymentHistory, paymentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

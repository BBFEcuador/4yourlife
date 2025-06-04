package com.foryourlife.admin.sales.payments.paymentMethod.infrastructure.httpControllers;

import com.foryourlife.admin.sales.payments.paymentMethod.application.PaymentMethodCommandService;
import com.foryourlife.admin.sales.payments.paymentMethod.application.PaymentMethodQueryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-method")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodCommandService paymentMethodCommandService;
    @Autowired
    private PaymentMethodQueryService paymentMethodQueryService;

    @GetMapping("")
    public ResponseEntity<?> getAllPaymentMethods() {
        return ResponseEntity.ok(paymentMethodQueryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentMethodById(String id) {
        return ResponseEntity.ok(paymentMethodQueryService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> createPaymentMethod(@RequestBody @Valid PaymentMethodRequest paymentMethod) {
        paymentMethodCommandService.createPaymentMethod(paymentMethod.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

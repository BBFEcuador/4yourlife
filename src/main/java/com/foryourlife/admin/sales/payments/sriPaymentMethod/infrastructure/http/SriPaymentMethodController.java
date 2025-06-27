package com.foryourlife.admin.sales.payments.sriPaymentMethod.infrastructure.http;

import com.foryourlife.admin.sales.payments.sriPaymentMethod.application.SriPaymentMethodQueryService;
import com.foryourlife.admin.sales.payments.sriPaymentMethod.domain.SriPaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sri-payment-method"  )
public class SriPaymentMethodController {

    @Autowired
    private SriPaymentMethodQueryService sriPaymentMethodService;

    @GetMapping("")
    public ResponseEntity<List<SriPaymentMethod>> getAllSriPaymentMethods() {
        return ResponseEntity.ok(sriPaymentMethodService.findAll());
    }
}

package com.foryourlife.admin.sales.payments.paymentMethod.infrastructure.httpControllers;

import com.foryourlife.admin.sales.payments.paymentMethod.application.PaymentMethodCommandService;
import com.foryourlife.admin.sales.payments.paymentMethod.application.PaymentMethodQueryService;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payment-method")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodCommandService paymentMethodCommandService;
    @Autowired
    private PaymentMethodQueryService paymentMethodQueryService;

    @GetMapping("")
    public ResponseEntity<?> getAllPaymentMethods(@RequestParam(value = "campusId",defaultValue = "") String campusId) {

        Criteria criteria = new Criteria(List.of(), Optional.empty(), Optional.empty());
        List<Filter> filters = new ArrayList<>();
        if (!campusId.isEmpty()) {
            filters.add(new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND));
        }
        criteria.filters = filters;

        return ResponseEntity.ok(paymentMethodQueryService.getAll(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentMethodById(String id) {
        return ResponseEntity.ok(paymentMethodQueryService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> createPaymentMethod(@RequestBody @Valid PaymentMethodRequest paymentMethod) {
        paymentMethodCommandService.createPaymentMethod(paymentMethod);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.foryourlife.admin.sales.payments.cashDrawerDetail.infrastructure.http;

import com.foryourlife.admin.sales.payments.cashDrawer.application.CashDrawerQueryService;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.application.CashDrawerDetailCommandService;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.application.CashDrawerDetailQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cash-drawer-detail")
public class CashDrawerDetailController {
    @Autowired
    private CashDrawerDetailCommandService commandService;
    @Autowired
    private CashDrawerDetailQueryService queryService;

    @GetMapping("{cashDrawerId}")
    public ResponseEntity<?> getByCashDrawerId(@PathVariable String cashDrawerId) {
        return new ResponseEntity<>(queryService.getByCashDrawerId(cashDrawerId), HttpStatus.OK);
    }
}

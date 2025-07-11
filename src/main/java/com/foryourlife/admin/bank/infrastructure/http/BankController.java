package com.foryourlife.admin.bank.infrastructure.http;

import com.foryourlife.admin.bank.application.BankCommandService;
import com.foryourlife.admin.bank.application.BankQueryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bank")
public class BankController {
    @Autowired
    private BankCommandService bankCommandService;
    @Autowired
    private BankQueryService bankQueryService;
    
    
    @GetMapping("")
    public ResponseEntity<?> getAllBanks() {
        return ResponseEntity.ok(bankQueryService.findAllBanks());
    }
    
    @PostMapping("")
    public ResponseEntity<?> createBank(@RequestBody @Valid BankRequest request) {
        bankCommandService.createBank(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{campusId}")
    public ResponseEntity<?> getBanksByCampus(String campusId) {
        return ResponseEntity.ok(bankQueryService.findByCampusId(campusId));
    }
}

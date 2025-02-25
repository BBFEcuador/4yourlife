package com.foryourlife.admin.auth.infrastructure.httpControllers;

import com.foryourlife.admin.auth.application.AdminCreateService;
import com.foryourlife.admin.auth.application.AdminFinderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminFinderService adminFinderService;
    private final AdminCreateService adminCreateService;

    public AdminController(AdminFinderService service, AdminCreateService adminCreateService) {
        this.adminFinderService = service;
        this.adminCreateService = adminCreateService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(adminFinderService.getAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateAdminRequest request) {
        this.adminCreateService.create(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/disabled")
    public ResponseEntity<?> disableAdmin(@RequestBody DisableAdminRequest disabled){
        adminCreateService.update(disabled);
    return new ResponseEntity<>(HttpStatus.OK);
    }
}

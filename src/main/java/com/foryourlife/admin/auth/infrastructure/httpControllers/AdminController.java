package com.foryourlife.admin.auth.infrastructure.httpControllers;

import com.foryourlife.admin.auth.application.AdminCreateService;
import com.foryourlife.admin.auth.application.AdminFinderService;
import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Page<Admin>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "campusId", defaultValue = "") String campusId
    ) {
        var pageable = PageRequest.of(page, perPage, Sort.by("user.name").ascending());
        if (search == null || search.trim().isEmpty()) {
            return new ResponseEntity<>(adminFinderService.findAll(pageable), HttpStatus.OK);
        }

        List<Filter> filters = new ArrayList<>();

        String[] terms = search.trim().toLowerCase().split("\\s+");

        if (!search.isEmpty()) {
            filters.addAll(List.of(
                    new Filter("email", search, "user", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                    new Filter("phone", search, "user", Filter.Operation.LIKE, Filter.LogicalOperator.OR)
            ));
        }

        for (String term : terms) {
            filters.add(new Filter("name", term, "user", Filter.Operation.LIKE, Filter.LogicalOperator.OR));
        }

        if (!campusId.isEmpty()) {
            filters.add(new Filter("campus.id", campusId, null, Filter.Operation.EQUAL, Filter.LogicalOperator.AND));
        }

        var criteria = new Criteria(filters, Optional.empty(), Optional.empty());
        var result = adminFinderService.findAll(pageable, criteria);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>getAdmin(@PathVariable String id){
        return new ResponseEntity<>(adminFinderService.findById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateAdminRequest request) {
        this.adminCreateService.create(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<?> updateAdmin(@Valid @RequestBody UpdateAdminRequest request) {
        this.adminCreateService.update(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/disabled")
    public ResponseEntity<?> disableAdmin(@RequestBody DisableAdminRequest disabled){
        adminCreateService.updateState(disabled);
    return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/pass")
    public ResponseEntity<?> updatePassAdmin(@RequestBody UpdatePassAdminRequest password){
        adminCreateService.updatePass(password);
    return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{adminId}/{roleId}")
    public ResponseEntity<?> changeRole(@PathVariable String adminId, @PathVariable String roleId){
        System.out.println(roleId);
        adminCreateService.changeRole(adminId, roleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.httpControllers;

import com.foryourlife.admin.sales.payments.cashDrawer.application.CashDrawerCommandService;
import com.foryourlife.admin.sales.payments.cashDrawer.application.CashDrawerQueryService;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/cash-drawer")
public class CashDrawerController {
    @Autowired
    private CashDrawerCommandService commandService;
    @Autowired
    private CashDrawerQueryService queryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCashDrawerById(@PathVariable String id) {
        return ResponseEntity.ok(queryService.getCashDrawerById(id));
    }

    @GetMapping("cash-box/{id}")
    public ResponseEntity<?> getCashDrawersByCashBoxId(
            @PathVariable String id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "search", defaultValue = "") String search
    ) {
        Criteria criteria = new Criteria(List.of(), Optional.empty(), Optional.empty());
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("id", id, "cashBox", Filter.Operation.EQUAL, Filter.LogicalOperator.AND));
        if (!search.isEmpty()) {
            filters.add(
                    new Filter("name", search, "openedByUser", Filter.Operation.LIKE, Filter.LogicalOperator.OR)
            );
        }
        criteria.filters = filters;
        var pageable = PageRequest.of(page, perPage, Sort.by("createdAt").descending());
        return ResponseEntity.ok(queryService.getAllCashDrawers(criteria, pageable));
    }

    @PutMapping("/open/{cashBoxId}")
    public ResponseEntity<CashDrawer> openDrawer(@PathVariable String cashBoxId, @RequestParam String userId, @RequestParam Double openingBalance, @RequestParam String detail) {
        return new ResponseEntity<>(commandService.openDrawer(cashBoxId, userId, openingBalance, detail), HttpStatus.OK);
    }

    @PutMapping("/close/{id}")
    public ResponseEntity<?> closeDrawer(@PathVariable String id, @RequestParam @Valid String userId) {
        ByteArrayOutputStream pdfBytes = commandService.closeDrawer(id, userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "cash-drawer-report.pdf");

        return new ResponseEntity<>(pdfBytes.toByteArray(), headers, HttpStatus.OK);
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<?> lockDrawer(@PathVariable String id, @RequestParam @Valid String pin) {
        commandService.lockCashDrawer(id, pin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/forget-pin/{id}")
    public ResponseEntity<?> forgetPin(@PathVariable String id) {
        commandService.forgetPin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("generate-report")
    public ResponseEntity<?> getDrawerReport(@RequestParam(value = "id", defaultValue = "") String id) {
        if (!id.isEmpty()) {
            ByteArrayOutputStream pdfBytes = commandService.getCloseReport(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "cash-drawer-report.pdf");

            return new ResponseEntity<>(pdfBytes.toByteArray(), headers, HttpStatus.OK);
        } else {
            throw new BaseException("Id esta vacío", List.of(""));
        }
    }

    @GetMapping("opened-user/{userId}")
    public ResponseEntity<?> getOpenedUser(@PathVariable String userId) {
        return new ResponseEntity<>(queryService.getCashDrawerOpenedByUser(userId), HttpStatus.OK);
    }
}

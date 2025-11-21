package com.foryourlife.admin.crm.call.infrastructure.http;

import com.foryourlife.admin.crm.call.application.CallLogCommandService;
import com.foryourlife.admin.crm.call.application.CallLogQueryService;
import com.foryourlife.admin.crm.call.domain.CallLog;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/call")
public class CallLogController {
    @Autowired
    private CallLogQueryService callLogQueryService;

    @Autowired
    private CallLogCommandService callLogCommandService;

    @PostMapping("")
    public ResponseEntity<?> logCall(@RequestBody @Valid CallLogRequest callLogRequest) {
        callLogCommandService.saveCallLog(callLogRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Page<CallLog>> getAllCallLogs(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "perPage", defaultValue = "10") int perPage, @RequestParam(value = "search", defaultValue = "") String search, @RequestParam(value = "campusId", defaultValue = "") String campusId) {
        var p = PageRequest.of(page, perPage, org.springframework.data.domain.Sort.by("createdAt").descending());

        Criteria criteria = new Criteria(List.of(), Optional.empty(), Optional.empty());

        List<Filter> filters = new ArrayList<>();
        if (!search.isEmpty()) {
            filters.addAll(
                    List.of(
                            new Filter("calledBy.name", search, "user", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                            new Filter("calledUser.name", search, "user", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                            new Filter("createdAt", search, null, Filter.Operation.LIKE, Filter.LogicalOperator.OR)
                    )
            );
        }
        criteria.filters = filters;
        return new ResponseEntity<>(callLogQueryService.findAll(criteria), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CallLog> getCallLogById(@PathVariable String id) {
        Optional<CallLog> callLog = callLogQueryService.findById(id);
        return callLog.map(log -> new ResponseEntity<>(log, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

package com.foryourlife.admin.crm.call.infrastructure.http;

import com.foryourlife.admin.crm.call.application.CallCommandService;
import com.foryourlife.admin.crm.call.application.CallQueryService;
import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallResponse;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("call")
public class CallController {
    @Autowired
    private CallCommandService callCommandService;

    @Autowired
    private CallQueryService callQueryService;

    @GetMapping("")
    public ResponseEntity<Page<Call>> findAllCall(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "campusId", defaultValue = "") String campusId
    ) {
        var p = PageRequest.of(page, perPage, Sort.by("createdAt").descending());

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
        return new ResponseEntity<>(callQueryService.findAll(p, criteria), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> logCall(@RequestBody CallRequest callRequest) {
        callCommandService.createCall(callRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("training/{id}")
    public ResponseEntity<?> findAllByTrainingId(@PathVariable String id) {
        var calls = callQueryService.findAllByTrainingId(id);
        return new ResponseEntity<>(calls, HttpStatus.OK);
    }
}

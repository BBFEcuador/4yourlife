package com.foryourlife.admin.crm.statement.infrastructure.http;

import com.foryourlife.admin.crm.statement.application.StatementCommandService;
import com.foryourlife.admin.crm.statement.application.StatementQueryService;
import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.admin.crm.statement.domain.StatementDTO;
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
@RequestMapping("/statements")
public class StatementController {
    @Autowired
    private StatementCommandService commandService;

    @Autowired
    private StatementQueryService queryService;

    @GetMapping("{trainingId}")
    public ResponseEntity<Page<StatementDTO>> findAll(
            @PathVariable String trainingId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "search", defaultValue = "") String search){
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        Criteria criteria = new Criteria(List.of(), Optional.empty(), Optional.empty());

        List<Filter> filters = new ArrayList<>();


        if (!search.isEmpty()) {
            filters.add(
                    new Filter("name", search, "participant.user", Filter.Operation.LIKE, Filter.LogicalOperator.OR)
            );
        }

        criteria.filters = filters;

        return new ResponseEntity<>(queryService.findAll(p, criteria, trainingId), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateStatus(@RequestBody StatementChangeStatusRequest request, @PathVariable String id){
        commandService.changeStatementStatus(request, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

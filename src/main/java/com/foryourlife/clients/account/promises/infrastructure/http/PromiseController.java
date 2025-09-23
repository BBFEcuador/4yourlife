package com.foryourlife.clients.account.promises.infrastructure.http;

import com.foryourlife.clients.account.promises.application.PromiseCommandService;
import com.foryourlife.clients.account.promises.application.PromiseQueryService;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/promises")
public class PromiseController {
    @Autowired
    private PromiseCommandService promiseCommandService;

    @Autowired
    private PromiseQueryService promiseQueryService;

    @PostMapping("")
    public ResponseEntity<Void> savePromise(PromiseRequest promiseRequest) {
        promiseCommandService.savePromise(promiseRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPromises(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "campusId", defaultValue = "") String campusId
    ) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        Criteria criteria = new Criteria(List.of(), Optional.empty(), Optional.empty());
        List<Filter> filters = new ArrayList<>();
        if (!search.isEmpty()) {
            filters.addAll(
                    List.of(
                            new Filter("name", search, "training", Filter.Operation.LIKE, Filter.LogicalOperator.OR),
                            new Filter("name", search, "participant", Filter.Operation.LIKE, Filter.LogicalOperator.OR)
                    )
            );
        }
        if (!campusId.isEmpty()) {
            filters.add(new Filter("id", campusId, "campus", Filter.Operation.EQUAL, Filter.LogicalOperator.AND));
        }
        criteria.filters = filters;
        return new ResponseEntity<>(promiseQueryService.findAll(p, criteria), HttpStatus.OK);
    }

    @PostMapping("training")
    public ResponseEntity<Void> createPromiseForTraining(@RequestParam String trainingId) {
        promiseCommandService.createPromises(trainingId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

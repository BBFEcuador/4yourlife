package com.foryourlife.clients.account.promises.infrastructure.http;

import com.foryourlife.clients.account.promises.application.PromiseCommandService;
import com.foryourlife.clients.account.promises.application.PromiseQueryService;
import com.foryourlife.clients.account.promises.domain.Promise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

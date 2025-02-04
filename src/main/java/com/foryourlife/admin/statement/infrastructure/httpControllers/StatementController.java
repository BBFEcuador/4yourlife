package com.foryourlife.admin.statement.infrastructure.httpControllers;

import com.foryourlife.admin.statement.application.StatementCreatorService;
import com.foryourlife.admin.statement.application.StatementFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statement")
public class StatementController {
    @Autowired
    private StatementCreatorService creatorService;

    @Autowired
    private StatementFinderService finderService;

    @PostMapping("/add")
    public void addStatement(@RequestBody StatementRequest statementRequest)  {
        creatorService.createStatement(statementRequest.toDomain());
    }
}

package com.foryourlife.admin.sales.rules.infraestructure.httpControllers;

import com.foryourlife.admin.sales.rules.application.CommandRuleService;
import com.foryourlife.admin.sales.rules.domain.RuleContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rule")
public class RuleController {
    @Autowired
    private CommandRuleService ruleService;

    @PostMapping("applied-rules")
    public ResponseEntity<?> getRules(@RequestBody @Valid RuleRequest request) {
        RuleContext context = RuleContext.autoFromSystem();

        return ResponseEntity.ok()
                .body(ruleService.getApplicableRules(request.getUserId(), request.getProductId(), context));
    }
}

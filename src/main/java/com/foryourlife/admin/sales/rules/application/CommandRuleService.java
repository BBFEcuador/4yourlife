package com.foryourlife.admin.sales.rules.application;

import com.foryourlife.admin.sales.rules.domain.Rule;
import com.foryourlife.admin.sales.rules.domain.RuleRepository;
import org.springframework.stereotype.Service;

@Service
public class CommandRuleService {
    private final RuleRepository _ruleRepository;

    public CommandRuleService(RuleRepository _ruleRepository) {
        this._ruleRepository = _ruleRepository;
    }

    public void save(Rule rule){
        _ruleRepository.save(rule);
    }

    public void disable(String id){
        _ruleRepository.disable(id);
    }
}

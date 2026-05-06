package com.foryourlife.admin.sales.rules.application;

import com.foryourlife.admin.sales.rules.domain.Rule;
import com.foryourlife.admin.sales.rules.domain.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryRuleService {
    private final RuleRepository _ruleRepository;

    public QueryRuleService(RuleRepository _ruleRepository) {
        this._ruleRepository = _ruleRepository;
    }

    public Rule findById(String id) {
        return _ruleRepository.findById(id);
    }

    public List<Rule> findAllByProductId(String id) {
        return _ruleRepository.findAllByProduct(id);
    }
}

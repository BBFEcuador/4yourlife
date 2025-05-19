package com.foryourlife.admin.sales.rules.infraestructure.persistence;

import com.foryourlife.admin.sales.rules.domain.Rule;
import com.foryourlife.admin.sales.rules.domain.RuleRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleRepositoryImpl implements RuleRepository {
    private final JPARuleRepository impl;

    public RuleRepositoryImpl(JPARuleRepository impl) {
        this.impl = impl;
    }

    public void save(Rule rule) {
        this.impl.save(rule);
    }

    public Rule findById(String id) {
        return this.impl.findById(id).orElseThrow(() -> new BaseException("Rule not found", List.of("")));
    }

    public void disable(String id) {
        Rule rule = findById(id);
        if (rule != null) {
            rule.setEnabled(false);
            save(rule);
        }
    }

    public List<Rule> findAllByProduct(String productId) {
        return this.impl.findAllByProductId(productId);
    }
}

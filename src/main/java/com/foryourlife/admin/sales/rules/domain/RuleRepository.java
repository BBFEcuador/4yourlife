package com.foryourlife.admin.sales.rules.domain;

import java.util.List;

public interface RuleRepository {
    void save(Rule rule);
    Rule findById(String id);
    void disable(String id);
    List<Rule> findAllByProduct(String productId);
}

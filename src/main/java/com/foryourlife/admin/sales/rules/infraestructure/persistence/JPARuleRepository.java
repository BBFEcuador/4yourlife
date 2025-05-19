package com.foryourlife.admin.sales.rules.infraestructure.persistence;

import com.foryourlife.admin.sales.rules.domain.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPARuleRepository extends JpaRepository<Rule,String> {
    List<Rule> findAllByProductId(String productId);
}

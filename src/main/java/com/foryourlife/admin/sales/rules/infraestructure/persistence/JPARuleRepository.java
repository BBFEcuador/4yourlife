package com.foryourlife.admin.sales.rules.infraestructure.persistence;

import com.foryourlife.admin.sales.rules.domain.Rule;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPARuleRepository extends JpaRepository<Rule,String> {
    List<Rule> findAllByProductId(String productId);
    @Query("SELECT r FROM Rule r WHERE r.product.id = :productId AND r.ruleType = :courseLevel AND r.enabled = TRUE ORDER BY r.value ASC")
    List<Rule> findAllApplicableRules(@Param("productId") String productId, @Param("courseLevel") CourseLevel courseLevel);

}

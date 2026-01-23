package com.foryourlife.admin.sales.product.infrastructure.persistence;

import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    Page<Product> findAllByIsActiveTrue(Pageable pageable);

    Optional<Product> findByContificoId(String contificoId);

    List<Product> findAllByCampus_Id(String campusId);

    @Query("""
                SELECT p
                FROM Product p
                JOIN p.programs pr
                WHERE p.campus.id = :campusId
                  AND pr.courseLevel = :courseLevel
                  AND p.isActive = true
                ORDER BY p.basePrice DESC
                limit 1
            """)
    Optional<Product> findTopPricedProductByCampusAndCourseLevel(
            @Param("campusId") String campusId,
            @Param("courseLevel") CourseLevel courseLevel
    );
}

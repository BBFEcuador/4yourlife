package com.foryourlife.admin.sales.product.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(String id);
    Optional<Product> findByContificoId(String contificoId);
    Page<Product> findAll(Pageable pageable, Criteria criteria);
    Page<Product> findAll(Pageable pageable);
    Page<Product> findAllAvailable(Pageable pageable);
    List<Product> findAllByCampusId(String campusId);
    List<Product> findAll();
    Optional<Product> findByCampusIdAndProductProgramAndHighPrice(String campusId, CourseLevel courseLevel);
    void saveAll(List<Product> products);
}

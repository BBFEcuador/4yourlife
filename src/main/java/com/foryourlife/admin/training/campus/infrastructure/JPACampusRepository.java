package com.foryourlife.admin.training.campus.infrastructure;

import com.foryourlife.admin.training.campus.domain.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPACampusRepository extends JpaRepository<Campus,String> {

}

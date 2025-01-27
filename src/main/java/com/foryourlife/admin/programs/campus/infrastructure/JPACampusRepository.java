package com.foryourlife.admin.programs.campus.infrastructure;

import com.foryourlife.admin.programs.campus.domain.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPACampusRepository extends JpaRepository<Campus,String> {

}

package com.foryourlife.clients.account.user.infrastructure;

import com.foryourlife.clients.account.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAUserRepository extends JpaRepository<Users,String>, JpaSpecificationExecutor<Users> {
    Optional<Users> findByEmail (String email);
}

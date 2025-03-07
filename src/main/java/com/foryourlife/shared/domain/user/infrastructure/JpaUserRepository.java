package com.foryourlife.shared.domain.user.infrastructure;

import com.foryourlife.shared.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository  extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);
}

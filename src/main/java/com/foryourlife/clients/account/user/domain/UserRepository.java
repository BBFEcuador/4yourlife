package com.foryourlife.clients.account.user.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    LoginResponse login(String username,String password) throws BaseException;
    Optional<Users> findByEmail(String email);
    Optional<Users> findById(String id);
    List<Users> getAll();
    List<Users> match(Criteria criteria);
    void save(Users user);
}

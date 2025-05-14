package com.foryourlife.clients.account.user.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    LoginResponse login(String username, String password) throws BaseException;

    Optional<Participant> findByEmail(String email);

    Optional<Participant> findById(String id);

    List<Participant> getAll();
    Page<Participant> getAll(Pageable pageable);

    List<Participant> match(Criteria criteria);

    void save(Participant user);

    Participant findByUserId(String userId);
}

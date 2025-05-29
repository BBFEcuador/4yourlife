package com.foryourlife.clients.account.participant.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {
    LoginResponse login(String username, String password) throws BaseException;

    Optional<Participant> findByEmail(String email);

    Optional<Participant> findById(String id);

    List<Participant> getAll();
    Page<Participant> getAll(Pageable pageable,Criteria criteria);

    List<Participant> match(Criteria criteria);

    void save(Participant user);

    Participant findByUserId(String userId);
}

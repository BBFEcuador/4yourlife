package com.foryourlife.clients.account.promises.domain;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface PromiseRepository {
    Promise save(Promise promise);
    Optional<Promise> findById(String id);
    void deleteById(String id);
    Page<Promise> findAll(Pageable pageable, Criteria criteria);
}

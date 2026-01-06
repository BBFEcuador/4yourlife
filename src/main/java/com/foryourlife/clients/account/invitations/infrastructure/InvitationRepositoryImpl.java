package com.foryourlife.clients.account.invitations.infrastructure;

import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvitationRepositoryImpl implements InvitationRepository {

    private final JPAInvitationRepository repository;
    private final JPACriteriaConverter<Invitation> converter;

    public InvitationRepositoryImpl(JPAInvitationRepository repository, JPACriteriaConverter<Invitation> converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public void save(Invitation invitation) {
        this.repository.save(invitation);
    }

    @Override
    public Optional<Invitation> findByToken(String token) {
        return this.repository.findByToken(token);
    }

    @Override
    public List<Invitation> findBySenderId(String token) {
        return this.repository.findBySenderId(token);
    }

    @Override
    public Optional<Invitation> findTopBySenderIdOrderByQuantityDesc(String id) {
        return this.repository.findTopBySenderIdOrderByQuantityDesc(id);
    }

    public List<Invitation> findAllByTokenIn(List<String> ids) {
        return repository.findAllByTokenIn(ids);
    }
}

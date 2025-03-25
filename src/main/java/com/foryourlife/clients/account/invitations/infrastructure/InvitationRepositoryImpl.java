package com.foryourlife.clients.account.invitations.infrastructure;

import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvitationRepositoryImpl implements InvitationRepository {

    private final JPAInvitationRepository repository;

    public InvitationRepositoryImpl(JPAInvitationRepository repository) {
        this.repository = repository;
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
}

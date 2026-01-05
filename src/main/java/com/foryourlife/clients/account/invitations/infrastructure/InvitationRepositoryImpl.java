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

    public List<Invitation> findAllByUsersIds(List<String> ids) {
        Criteria c = new Criteria(
                List.of(
                        new Filter(
                                "senderId",
                                String.join(",", ids),
                                null,
                                Filter.Operation.IN,
                                Filter.LogicalOperator.AND
                        )
                ),
                Optional.of(0),
                Optional.of(1)
        );
        var temp = repository.findAll(converter.getJpaSpecifications(c), converter.getJpaPageable(c));
        Criteria cc = new Criteria(
                List.of(
                        new Filter(
                                "senderId",
                                String.join(",", ids),
                                null,
                                Filter.Operation.IN,
                                Filter.LogicalOperator.AND
                        )
                ),
                Optional.of(0),
                Optional.of((int) temp.getTotalElements())
        );
        return repository.findAll(converter.getJpaSpecifications(cc), converter.getJpaPageable(cc)).getContent();
    }
}

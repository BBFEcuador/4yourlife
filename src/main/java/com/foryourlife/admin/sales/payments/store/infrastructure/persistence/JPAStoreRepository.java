package com.foryourlife.admin.sales.payments.store.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.store.domain.Store;
import com.foryourlife.admin.sales.payments.store.domain.StoreRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPAStoreRepository implements StoreRepository {
    private final JPAImplStoreRepository repository;
    private final JPACriteriaConverter<Store> criteriaConverter;

    public JPAStoreRepository(JPAImplStoreRepository repository, JPACriteriaConverter<Store> criteriaConverter) {
        this.repository = repository;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public Store save(Store store) {
        return repository.save(store);
    }

    @Override
    public Optional<Store> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<Store> getByCampusId(String campusId) {
        return repository.findAllByCampus_Id(campusId);
    }

    @Override
    public Optional<Store> findByEstablishment(String campusId, String name) {
        return repository.findByCampus_IdAndNumber(campusId, name);
    }

    @Override
    public Page<Store> getAll(Pageable pageable, Criteria criteria) {
        return repository.findAll(
                criteriaConverter.getJpaSpecifications(criteria),
                pageable
        );
    }
}

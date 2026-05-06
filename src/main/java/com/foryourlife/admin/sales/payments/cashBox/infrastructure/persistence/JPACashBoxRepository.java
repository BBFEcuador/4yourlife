package com.foryourlife.admin.sales.payments.cashBox.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashBox.domain.CashBox;
import com.foryourlife.admin.sales.payments.cashBox.domain.CashBoxRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPACashBoxRepository implements CashBoxRepository {
    private final JPAImplCashBoxRepository repository;

    public JPACashBoxRepository(JPAImplCashBoxRepository repository) {
        this.repository = repository;
    }

    @Override
    public CashBox save(CashBox cashBox) {
        return repository.save(cashBox);
    }

    @Override
    public Optional<CashBox> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<CashBox> findByNumberAndStoreId(String number,  String storeId) {
        return repository.findByStore_IdAndNumber(storeId, number);
    }

    @Override
    public List<CashBox> findAll() {
        return repository.findAll();
    }

    @Override
    public List<CashBox> findAllByCampus(String campusId) {
        return repository.findAllByStore_Campus_Id(campusId);
    }

    @Override
    public List<CashBox> findAllByStoreId(String storeId) {
        return repository.findAllByStore_Id(storeId);
    }
}

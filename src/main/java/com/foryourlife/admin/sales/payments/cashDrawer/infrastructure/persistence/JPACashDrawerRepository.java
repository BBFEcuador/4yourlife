package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPACashDrawerRepository implements CashDrawerRepository {
    private final JPAImplCashDrawerRepository repository;

    public JPACashDrawerRepository(JPAImplCashDrawerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(CashDrawer cashDrawer) {
        this.repository.save(cashDrawer);
    }

    @Override
    public Optional<CashDrawer> getById(String id) {
        return this.repository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        this.repository.deleteById(id);
    }

    @Override
    public List<CashDrawer> getAll() {
        return this.repository.findAll();
    }

    @Override
    public List<CashDrawer> getByOpenedByUserId(String userId) {
        return this.repository.findAllByOpenedByUser_Id(userId);
    }

    @Override
    public List<CashDrawer> getByClosedByUserId(String userId) {
        return this.repository.findAllByClosedByUser_Id(userId);
    }

    @Override
    public Boolean findByNumber(String number) {
        return this.repository.existsCashDrawerByNumber(number);
    }

    @Override
    public Boolean getByIsOpenAndByUserId(String userId) {
        return this.repository.existsByStatusAndOpenedByUser_Id(CashDrawerStatus.OPEN, userId);
    }
}

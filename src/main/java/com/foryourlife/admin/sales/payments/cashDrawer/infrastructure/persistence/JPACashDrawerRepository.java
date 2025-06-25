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
    public CashDrawer save(CashDrawer cashDrawer) {
        return this.repository.save(cashDrawer);
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
    public List<CashDrawer> getByUserIdAndStatusOpen(String userid) {
        return this.repository.findAllByStatusAndOpenedByUser_Id(CashDrawerStatus.OPEN,userid);
    }

    @Override
    public Optional<CashDrawer> getByIsOpenAndByUserId(String userId) {
        return this.repository.findByStatusAndOpenedByUser_Id(CashDrawerStatus.OPEN, userId);
    }

    @Override
    public Optional<CashDrawer> findByCashBoxIdAndStatus(String id, CashDrawerStatus status) {
        return repository.findByStatusIsAndCashBox_Id(status, id);
    }

    @Override
    public List<CashDrawer> getByCashBoxId(String id) {
        return this.repository.findAllByCashBox_Id(id);
    }
}

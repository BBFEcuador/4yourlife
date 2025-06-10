package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JPACashDrawerRepository implements CashDrawerRepository {
  private JPAImplCashDrawerRepository repository;

    public JPACashDrawerRepository(JPAImplCashDrawerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(CashDrawer cashDrawer) {
        this.repository.save(cashDrawer);
    }

    @Override
    public CashDrawer getById(String id) {
        return this.repository.getById(id);
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
}

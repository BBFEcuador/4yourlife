package com.foryourlife.admin.sales.payments.cashDrawerDetail.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JPACashDrawerDetailRepository implements CashDrawerDetailRepository {
    private final JPAImlCashDrawerDetailRepository repository;

    public JPACashDrawerDetailRepository(JPAImlCashDrawerDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(CashDrawerDetail cashDrawerDetail) {
        repository.save(cashDrawerDetail);
    }

    @Override
    public List<CashDrawerDetail> findAllByCashDrawerId(String paymentHistoryId) {
        return repository.findAllByCashDrawer_Id(paymentHistoryId);
    }

    @Override
    public void deleteByPaymentHistoryId(String paymentHistoryId) {
        repository.deleteById(paymentHistoryId);
    }
}

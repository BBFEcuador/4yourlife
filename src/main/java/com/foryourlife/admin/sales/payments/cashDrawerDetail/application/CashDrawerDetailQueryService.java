package com.foryourlife.admin.sales.payments.cashDrawerDetail.application;

import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashDrawerDetailQueryService {
    private final CashDrawerDetailRepository repository;

    public CashDrawerDetailQueryService(CashDrawerDetailRepository repository) {
        this.repository = repository;
    }

    public List<CashDrawerDetail> getByCashDrawerId(String cashDrawerId) {
        return repository.findAllByCashDrawerId(cashDrawerId);
    }
}

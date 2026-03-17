package com.foryourlife.admin.sales.payments.cashDrawer.application;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CashDrawerQueryService {
    private final CashDrawerRepository repository;

    public CashDrawerQueryService(CashDrawerRepository repository) {
        this.repository = repository;
    }

    public Page<CashDrawer> getAllCashDrawers(Criteria criteria, Pageable pageable) {
        return repository.getAll(criteria, pageable);
    }

    public CashDrawer getCashDrawerById(String id) {
        return repository.getById(id).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of("The cash drawer with id " + id + " does not exist"))
        );
    }

    public Page<CashDrawer> getCashDrawersByCashBoxId(Pageable pageable, String id) {
        return repository.getByCashBoxId(pageable, id);
    }

    @Transactional(readOnly = true)
    public Optional<CashDrawer> getCashDrawerOpenedByUser(String userId) {
        return repository.findByStatusIsAndOpenedByUserId(CashDrawerStatus.OPEN, userId);
    }
}

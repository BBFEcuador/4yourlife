package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPAImplCashDrawerRepository extends JpaRepository<CashDrawer, String> {
    List<CashDrawer> findAllByOpenedByUser_Id(String openedByUserId);

    List<CashDrawer> findAllByClosedByUser_Id(String closedByUserId);
}

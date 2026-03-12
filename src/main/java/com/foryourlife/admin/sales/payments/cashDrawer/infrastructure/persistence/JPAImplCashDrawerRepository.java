package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.shared.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

interface JPAImplCashDrawerRepository extends JpaRepository<CashDrawer, String>, JpaSpecificationExecutor<CashDrawer> {

    Optional<CashDrawer> findByStatusIsAndCashBox_Id(CashDrawerStatus status, String cashBoxId);

    Optional<CashDrawer> findAllByStatusIsNotAndOpenedByUser_Id(CashDrawerStatus status, String openedByUserId);

    Optional<CashDrawer> findByStatusIsAndCashBox_IdAndOpenedByUser_Id(CashDrawerStatus status, String cashBoxId, String openedByUserId);

    Page<CashDrawer> findAllByCashBox_Id(String cashBoxId, Pageable pageable);
}

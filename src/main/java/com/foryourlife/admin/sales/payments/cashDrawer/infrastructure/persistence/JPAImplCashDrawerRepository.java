package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.shared.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface JPAImplCashDrawerRepository extends JpaRepository<CashDrawer, String> {

    Boolean existsByStatusAndOpenedByUser_Id(CashDrawerStatus status, String openedByUserId);

    Optional<CashDrawer> findByStatusIsAndCashBox_Id(CashDrawerStatus status, String cashBoxId);

    List<CashDrawer> findAllByCashBox_Id(String cashBoxId);

    List<CashDrawer> findAllByOpenedByUser_IdAndStatus(String openedByUserId, CashDrawerStatus status);

    List<CashDrawer> findAllByStatusAndOpenedByUser(CashDrawerStatus status, User openedByUser);

    List<CashDrawer> findAllByStatusAndOpenedByUser_Id(CashDrawerStatus status, String openedByUserId);

    Optional<CashDrawer> findByStatusAndOpenedByUser_Id(CashDrawerStatus status, String openedByUserId);
}

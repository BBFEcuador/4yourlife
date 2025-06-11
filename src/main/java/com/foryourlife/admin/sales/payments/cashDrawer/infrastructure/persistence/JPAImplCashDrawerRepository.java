package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.shared.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPAImplCashDrawerRepository extends JpaRepository<CashDrawer, String> {
    List<CashDrawer> findAllByOpenedByUser_Id(String openedByUserId);

    List<CashDrawer> findAllByClosedByUser_Id(String closedByUserId);

    Boolean existsCashDrawerByNumber(String number);

    Boolean existsByIsOpenIsTrueAndOpenedByUser(User openedByUser);

    Boolean existsByIsOpenIsTrueAndOpenedByUser_Id(String userId);
}

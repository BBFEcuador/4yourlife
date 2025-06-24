package com.foryourlife.admin.sales.payments.cashDrawerDetail.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPAImlCashDrawerDetailRepository extends JpaRepository<CashDrawerDetail, String> {
    List<CashDrawerDetail> findAllByCashDrawer_Id(String cashDrawerId);
}

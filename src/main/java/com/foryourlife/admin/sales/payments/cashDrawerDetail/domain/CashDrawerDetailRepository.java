package com.foryourlife.admin.sales.payments.cashDrawerDetail.domain;

import java.util.List;

public interface CashDrawerDetailRepository {
    void save(CashDrawerDetail cashDrawerDetail);
    List<CashDrawerDetail> findAllByCashDrawerId(String paymentHistoryId);
    void deleteByPaymentHistoryId(String paymentHistoryId);
}

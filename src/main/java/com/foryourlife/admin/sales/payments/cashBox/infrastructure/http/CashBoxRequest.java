package com.foryourlife.admin.sales.payments.cashBox.infrastructure.http;

import com.foryourlife.admin.sales.payments.cashBox.domain.CashBox;
import com.foryourlife.admin.sales.payments.store.domain.Store;
import com.foryourlife.shared.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CashBoxRequest {
    public String id;
    @NotNull(message = "The number field is required")
    @NotBlank(message = "The number field is required")
    public String number;
    public Integer firstNumberInvoice;
    public Store store;

    public CashBox toDomain() {
        return CashBox.create(id != null ? id : UUID.randomUUID().toString(), number, true, firstNumberInvoice != null ? firstNumberInvoice : 0, store);
    }
}

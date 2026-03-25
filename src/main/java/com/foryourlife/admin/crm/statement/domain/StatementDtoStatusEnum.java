package com.foryourlife.admin.crm.statement.domain;

public enum StatementDtoStatusEnum {
    FULL_PAYMENT("Pago total"),
    PAYMENT("Abono"),
    NOT_PAYMENT("Sin pago");

    private final String value;

    StatementDtoStatusEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}

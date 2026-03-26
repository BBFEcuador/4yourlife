package com.foryourlife.admin.crm.statement.domain;

public enum StatementStatusEnum {
    NO_INTERESTED("No Interesa"),
    EMPTY("Vacio"),
    CONFIRMED("Confirmado"),
    POSSIBILITY("Posibilidad"),
    AGREEMENT("Acuerdo");

    private final String value;

    StatementStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;               }
}

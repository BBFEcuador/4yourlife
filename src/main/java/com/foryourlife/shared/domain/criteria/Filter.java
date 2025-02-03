package com.foryourlife.shared.domain.criteria;

public class Filter {
    public final String column;
    public final String value;
    public final String joinTable;
    public final Operation operation;

    public Filter(String column, String value, String joinTable, Operation operation) {
        this.column = column;
        this.value = value;
        this.joinTable = joinTable;
        this.operation = operation;
    }

    public enum Operation{
        EQUAL, LIKE, IN, GREATER_THAN, LESS_THAN, BETWEEN, JOIN;
    }

    public String getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }

    public String getJoinTable() {
        return joinTable;
    }

    public Operation getOperation() {
        return operation;
    }
}

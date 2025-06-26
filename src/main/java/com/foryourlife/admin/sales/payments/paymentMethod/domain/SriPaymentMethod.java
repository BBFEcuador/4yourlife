package com.foryourlife.admin.sales.payments.paymentMethod.domain;

public enum SriPaymentMethod {
    EFECTIVO("SIN UTILIZACION DEL SISTEMA FINANCIERO", "01", "Efectivo", 1),
    CHEQUE_PROPIO("CHEQUE PROPIO", "02", "Cheque propio", 0),
    CHEQUE_CERTIFICADO("CHEQUE CERTIFICADO", "03", "Cheque certificado", 0),
    CHEQUE_DE_GERENCIA("CHEQUE DE GERENCIA", "04", "Cheque de gerencia", 0),
    CHEQUE_DEL_EXTERIOR("CHEQUE DEL EXTERIOR", "05", "Cheque del exterior", 0),
    DEBITO_DE_CUENTA("DÉBITO DE CUENTA", "06", "Débito de cuenta", 0),
    TRANSFERENCIA_PROPIO_BANCO("TRANSFERENCIA PROPIO BANCO", "07", "Transferencia propio banco", 0),
    TRANSFERENCIA_OTRO_BANCO_NACIONAL("TRANSFERENCIA OTRO BANCO NACIONAL", "08", "Transferencia otro banco nacional", 0),
    TRANSFERENCIA_BANCO_EXTERIOR("TRANSFERENCIA BANCO EXTERIOR", "09", "Transferencia banco exterior", 0),
    TARJETA_DE_CREDITO_NACIONAL("TARJETA DE CRÉDITO NACIONAL", "10", "Tarjeta de crédito nacional", 0),
    TARJETA_DE_CREDITO_INTERNACIONAL("TARJETA DE CRÉDITO INTERNACIONAL", "11", "Tarjeta de crédito internacional", 0),
    GIRO("GIRO", "12", "Giro", 0),
    DEPOSITO_EN_CUENTA("DEPOSITO EN CUENTA (CORRIENTE/AHORROS)", "13", "Depósito en cuenta", 0),
    ENDOSO_DE_INVERSION("ENDOSO DE INVERSIÓN", "14", "Endoso de inversión", 0),
    COMPENSACION_DE_DEUDAS("COMPENSACIÓN DE DEUDAS", "15", "Compensación de deudas", 8),
    TARJETA_DE_DEBITO("TARJETA DE DÉBITO", "16", "Tarjeta de débito", 4),
    DINERO_ELECTRONICO("DINERO ELECTRÓNICO", "17", "Dinero electrónico", 6),
    TARJETA_PREPAGO("TARJETA PREPAGO", "18", "Tarjeta prepago", 7),
    TARJETA_DE_CREDITO("TARJETA DE CRÉDITO", "19", "Tarjeta de crédito", 3),
    TRANSFERENCIA("OTROS CON UTILIZACION DEL SISTEMA FINANCIERO", "20", "Transferencia", 2),
    CHEQUE("OTROS CON UTILIZACION DEL SISTEMA FINANCIERO", "20", "Cheque", 5),
    ENDOSO_DE_TITULOS("ENDOSO DE TÍTULOS", "21", "Endoso de títulos", 9);

    private final String method;
    private final String code;
    private final String name;
    private final int priority;

    SriPaymentMethod(String method, String code, String name, int priority) {
        this.method = method;
        this.code = code;
        this.name = name;
        this.priority = priority;
    }

    public String getMethod() {
        return method;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }
}
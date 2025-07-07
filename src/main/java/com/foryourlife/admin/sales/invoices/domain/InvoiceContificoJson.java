package com.foryourlife.admin.sales.invoices.domain;

import java.util.List;

public class InvoiceContificoJson {
    public String pos; // la api token
    public String fecha_emision;
    public String tipo_documento; // FAC
    public String documento;
    public String autorizacion; // El numero de 49 digitos
    public Cliente cliente;
    public double subtotal_0; // 2 decimales
    public double subtotal_15; // 2 decimales
    public double ice; // 2 decimales
    public double iva; // 2 decimales
    public double total; // 2 decimales
    public List<Detalle> detalles;
    public double base_no_gravable;

    public static class Cliente {
        public String ruc; // required
        public String cedula; // required
        public String razon_social; // required
        public String telefonos;
        public String direccion;
        public String tipo; // required
        public String email;
        public boolean es_extranjero;
    }

    public static class Detalle {
        public String producto_id; // required
        public double cantidad; // required
        public double precio; // required
        public int porcentaje_iva;
        public double porcentaje_descuento; // required
        public double base_cero; // required
        public double base_gravable;
        public double base_no_gravable; // required
        public int porcentaje_ice;
        public Double valor_ice;
    }

    public InvoiceContificoJson(String pos, String fecha_emision, String tipo_documento, String documento, String autorizacion, Cliente cliente, double subtotal_0, double subtotal_15, double ice, double iva, double total, List<Detalle> detalles, double base_no_gravable) {
        this.pos = pos;
        this.fecha_emision = fecha_emision;
        this.tipo_documento = tipo_documento;
        this.documento = documento;
        this.autorizacion = autorizacion;
        this.cliente = cliente;
        this.subtotal_0 = subtotal_0;
        this.subtotal_15 = subtotal_15;
        this.ice = ice;
        this.iva = iva;
        this.total = total;
        this.detalles = detalles;
        this.base_no_gravable = base_no_gravable;
    }
}

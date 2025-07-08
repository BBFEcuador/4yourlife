package com.foryourlife.admin.sales.invoices.domain;

import java.util.List;

public class InvoiceContificoJson {
    public String pos;
    public String fecha_emision;
    public String tipo_documento;
    public String documento;
    public String autorizacion;
    public Cliente cliente;
    public double subtotal_0;
    public double subtotal_15;
    public double ice;
    public double iva;
    public double total;
    public List<Detalle> detalles;
    public double base_no_gravable;

    public static class Cliente {
        public String ruc;
        public String cedula;
        public String razon_social;
        public String telefonos;
        public String direccion;
        public String tipo; // required
        public String email;

        public Cliente(String cedula, String razon_social, String telefonos, String direccion, String tipo, String email) {
            this.cedula = cedula;
            this.razon_social = razon_social;
            this.telefonos = telefonos;
            this.direccion = direccion;
            this.tipo = tipo;
            this.email = email;
        }
    }

    public static class Detalle {
        public String producto_id;
        public double cantidad;
        public double precio;
        public int porcentaje_iva;
        public double porcentaje_descuento;
        public double base_cero;
        public double base_gravable;
        public double base_no_gravable;
        public int porcentaje_ice;
        public Double valor_ice;
        public Detalle(String producto_id, double cantidad, double precio, int porcentaje_iva, double porcentaje_descuento, double base_cero, double base_gravable, double base_no_gravable, int porcentaje_ice, Double valor_ice) {
            this.producto_id = producto_id;
            this.cantidad = cantidad;
            this.precio = precio;
            this.porcentaje_iva = porcentaje_iva;
            this.porcentaje_descuento = porcentaje_descuento;
            this.base_cero = base_cero;
            this.base_gravable = base_gravable;
            this.base_no_gravable = base_no_gravable;
            this.porcentaje_ice = porcentaje_ice;
            this.valor_ice = valor_ice;
        }
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

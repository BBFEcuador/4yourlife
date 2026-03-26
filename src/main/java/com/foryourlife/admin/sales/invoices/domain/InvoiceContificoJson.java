package com.foryourlife.admin.sales.invoices.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class InvoiceContificoJson implements Serializable {
    public String pos;
    public String fecha_emision;
    public String tipo_documento;
    public String documento;
    public String autorizacion;
    public Cliente cliente;
    public BigDecimal subtotal_0;
    public BigDecimal subtotal_12;
    public BigDecimal ice;
    public BigDecimal iva;
    public BigDecimal total;
    public String descripcion = null;
    public List<Detalle> detalles;
    public BigDecimal base_no_gravable;
    public String adicional1;
    public String estado;
    public boolean electronico = true;
    public List<Cobros> cobros;

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

    public InvoiceContificoJson(String pos, String fecha_emision, String tipo_documento, String documento, String autorizacion, Cliente cliente, BigDecimal subtotal_0, BigDecimal subtotal_12, BigDecimal ice, BigDecimal iva, BigDecimal total, String descripcion, List<Detalle> detalles, BigDecimal base_no_gravable, String adicional1, String estado, boolean electronico, List<Cobros> cobros) {
        this.pos = pos;
        this.fecha_emision = fecha_emision;
        this.tipo_documento = tipo_documento;
        this.documento = documento;
        this.autorizacion = autorizacion;
        this.cliente = cliente;
        this.subtotal_0 = subtotal_0;
        this.subtotal_12 = subtotal_12;
        this.ice = ice;
        this.iva = iva;
        this.total = total;
        this.descripcion = descripcion;
        this.detalles = detalles;
        this.base_no_gravable = base_no_gravable;
        this.adicional1 = adicional1;
        this.estado = estado;
        this.electronico = electronico;
        this.cobros = cobros;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public static class Cobros {
        public String forma_cobro;
        public BigDecimal monto;
        public String cuenta_bancaria_id;
        public String numero_comprobante;
        public String fecha;
        public String tipo_ping;
        public String numero_cheque;

        public String getNumero_cheque() {
            return numero_cheque;
        }

        public void setNumero_cheque(String numero_cheque) {
            this.numero_cheque = numero_cheque;
        }

        public String getForma_cobro() {
            return forma_cobro;
        }

        public void setForma_cobro(String forma_cobro) {
            this.forma_cobro = forma_cobro;
        }

        public BigDecimal getMonto() {
            return monto;
        }

        public void setMonto(BigDecimal monto) {
            this.monto = monto;
        }

        public String getCuenta_bancaria_id() {
            return cuenta_bancaria_id;
        }

        public void setCuenta_bancaria_id(String cuenta_bancaria_id) {
            this.cuenta_bancaria_id = cuenta_bancaria_id;
        }

        public String getNumero_comprobante() {
            return numero_comprobante;
        }

        public void setNumero_comprobante(String numero_comprobante) {
            this.numero_comprobante = numero_comprobante;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getTipo_ping() {
            return tipo_ping;
        }

        public void setTipo_ping(String tipo_ping) {
            this.tipo_ping = tipo_ping;
        }
    }
}

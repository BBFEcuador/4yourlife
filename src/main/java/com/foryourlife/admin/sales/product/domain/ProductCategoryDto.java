package com.foryourlife.admin.sales.product.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCategoryDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("padre_id")
    private String padreId;

    @JsonProperty("agrupar")
    private boolean agrupar;

    @JsonProperty("tipo_producto")
    private String tipoProducto;

    @JsonProperty("cuenta_venta")
    private String cuentaVenta;

    @JsonProperty("cuenta_compra")
    private String cuentaCompra;

    @JsonProperty("cuenta_inventario")
    private String cuentaInventario;

    public ProductCategoryDto() {
    }

    public ProductCategoryDto(String id, String nombre, String padreId, boolean agrupar, String tipoProducto, String cuentaVenta, String cuentaCompra, String cuentaInventario) {
        this.id = id;
        this.nombre = nombre;
        this.padreId = padreId;
        this.agrupar = agrupar;
        this.tipoProducto = tipoProducto;
        this.cuentaVenta = cuentaVenta;
        this.cuentaCompra = cuentaCompra;
        this.cuentaInventario = cuentaInventario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPadreId() {
        return padreId;
    }

    public void setPadreId(String padreId) {
        this.padreId = padreId;
    }

    public boolean isAgrupar() {
        return agrupar;
    }

    public void setAgrupar(boolean agrupar) {
        this.agrupar = agrupar;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getCuentaVenta() {
        return cuentaVenta;
    }

    public void setCuentaVenta(String cuentaVenta) {
        this.cuentaVenta = cuentaVenta;
    }

    public String getCuentaCompra() {
        return cuentaCompra;
    }

    public void setCuentaCompra(String cuentaCompra) {
        this.cuentaCompra = cuentaCompra;
    }

    public String getCuentaInventario() {
        return cuentaInventario;
    }

    public void setCuentaInventario(String cuentaInventario) {
        this.cuentaInventario = cuentaInventario;
    }
}



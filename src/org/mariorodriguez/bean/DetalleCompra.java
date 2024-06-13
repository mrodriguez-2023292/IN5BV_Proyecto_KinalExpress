
package org.mariorodriguez.bean;

public class DetalleCompra {
    private int codigoDetalleCompra;
    private double costoUnitario;
    private int cantidad;
    private String Productos_codigoProducto;
    private int Compras_numeroDocumento;
    
    public DetalleCompra() {
    }

    public DetalleCompra(int codigoDetalleCompra, double costoUnitario, int cantidad, String Productos_codigoProducto, int Compras_numeroDocumento) {
        this.codigoDetalleCompra = codigoDetalleCompra;
        this.costoUnitario = costoUnitario;
        this.cantidad = cantidad;
        this.Productos_codigoProducto = Productos_codigoProducto;
        this.Compras_numeroDocumento = Compras_numeroDocumento;
    }

    public int getCodigoDetalleCompra() {
        return codigoDetalleCompra;
    }

    public void setCodigoDetalleCompra(int codigoDetalleCompra) {
        this.codigoDetalleCompra = codigoDetalleCompra;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getProductos_codigoProducto() {
        return Productos_codigoProducto;
    }

    public void setProductos_codigoProducto(String Productos_codigoProducto) {
        this.Productos_codigoProducto = Productos_codigoProducto;
    }

    public int getCompras_numeroDocumento() {
        return Compras_numeroDocumento;
    }

    public void setCompras_numeroDocumento(int Compras_numeroDocumento) {
        this.Compras_numeroDocumento = Compras_numeroDocumento;
    }
}

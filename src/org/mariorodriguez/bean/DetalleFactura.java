
package org.mariorodriguez.bean;

public class DetalleFactura {
    private int codigoDetalleFactura;
    private double precioUnitario;
    private int cantidad;
    private int Factura_numeroFactura;
    private String Productos_codigoProducto;

    public DetalleFactura() {
    }

    public DetalleFactura(int codigoDetalleFactura, double precioUnitario, int cantidad, int Factura_numeroFactura, String Productos_codigoProducto) {
        this.codigoDetalleFactura = codigoDetalleFactura;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.Factura_numeroFactura = Factura_numeroFactura;
        this.Productos_codigoProducto = Productos_codigoProducto;
    }

    public int getCodigoDetalleFactura() {
        return codigoDetalleFactura;
    }

    public void setCodigoDetalleFactura(int codigoDetalleFactura) {
        this.codigoDetalleFactura = codigoDetalleFactura;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getFactura_numeroFactura() {
        return Factura_numeroFactura;
    }

    public void setFactura_numeroFactura(int Factura_numeroFactura) {
        this.Factura_numeroFactura = Factura_numeroFactura;
    }

    public String getProductos_codigoProducto() {
        return Productos_codigoProducto;
    }

    public void setProductos_codigoProducto(String Productos_codigoProducto) {
        this.Productos_codigoProducto = Productos_codigoProducto;
    }
}


package org.mariorodriguez.bean;

public class Productos {
    private String codigoProducto;
    private String descripcionProducto;
    private double precioUnitario;
    private double precioDocena;
    private double precioMayor;
    private int existencia;
    private int TipoProducto_codigoTipoProducto;
    private int Proveedores_codigoProveedor;

    public Productos() {
    }

    public Productos(String codigoProducto, String descripcionProducto, double precioUnitario, double precioDocena, double precioMayor, int existencia, int TipoProducto_codigoTipoProducto, int Proveedores_codigoProveedor) {
        this.codigoProducto = codigoProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioUnitario = precioUnitario;
        this.precioDocena = precioDocena;
        this.precioMayor = precioMayor;
        this.existencia = existencia;
        this.TipoProducto_codigoTipoProducto = TipoProducto_codigoTipoProducto;
        this.Proveedores_codigoProveedor = Proveedores_codigoProveedor;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getPrecioDocena() {
        return precioDocena;
    }

    public void setPrecioDocena(double precioDocena) {
        this.precioDocena = precioDocena;
    }

    public double getPrecioMayor() {
        return precioMayor;
    }

    public void setPrecioMayor(double precioMayor) {
        this.precioMayor = precioMayor;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public int getTipoProducto_codigoTipoProducto() {
        return TipoProducto_codigoTipoProducto;
    }

    public void setTipoProducto_codigoTipoProducto(int TipoProducto_codigoTipoProducto) {
        this.TipoProducto_codigoTipoProducto = TipoProducto_codigoTipoProducto;
    }

    public int getProveedores_codigoProveedor() {
        return Proveedores_codigoProveedor;
    }

    public void setProveedores_codigoProveedor(int Proveedores_codigoProveedor) {
        this.Proveedores_codigoProveedor = Proveedores_codigoProveedor;
    }
    
    @Override
    public String toString() {
        return ">> " + getDescripcionProducto();
    }
}

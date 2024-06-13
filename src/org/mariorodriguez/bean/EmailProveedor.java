
package org.mariorodriguez.bean;

public class EmailProveedor {
    private int codigoEmailProveedor;
    private String emailProveedor;
    private String descripcion;
    private int Proveedores_codigoProveedor;
   
    public EmailProveedor() {
    }

    public EmailProveedor(int codigoEmailProveedor, String emailProveedor, String descripcion, int Proveedores_codigoProveedor) {
        this.codigoEmailProveedor = codigoEmailProveedor;
        this.emailProveedor = emailProveedor;
        this.descripcion = descripcion;
        this.Proveedores_codigoProveedor = Proveedores_codigoProveedor;
    }

    public int getCodigoEmailProveedor() {
        return codigoEmailProveedor;
    }

    public void setCodigoEmailProveedor(int codigoEmailProveedor) {
        this.codigoEmailProveedor = codigoEmailProveedor;
    }

    public String getEmailProveedor() {
        return emailProveedor;
    }

    public void setEmailProveedor(String emailProveedor) {
        this.emailProveedor = emailProveedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getProveedores_codigoProveedor() {
        return Proveedores_codigoProveedor;
    }

    public void setProveedores_codigoProveedor(int Proveedores_codigoProveedor) {
        this.Proveedores_codigoProveedor = Proveedores_codigoProveedor;
    }
}


package org.mariorodriguez.bean;

public class Factura {
    private int numeroFactura;
    private String estado;
    private double totalFactura;
    private String fechaFactura;
    private int Clientes_codigoCliente;
    private int Empleados_codigoEmpleado;
    
    public Factura() {
    }

    public Factura(int numeroFactura, String estado, double totalFactura, String fechaFactura, int Clientes_codigoCliente, int Empleados_codigoEmpleado) {
        this.numeroFactura = numeroFactura;
        this.estado = estado;
        this.totalFactura = totalFactura;
        this.fechaFactura = fechaFactura;
        this.Clientes_codigoCliente = Clientes_codigoCliente;
        this.Empleados_codigoEmpleado = Empleados_codigoEmpleado;
    }

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(double totalFactura) {
        this.totalFactura = totalFactura;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public int getClientes_codigoCliente() {
        return Clientes_codigoCliente;
    }

    public void setClientes_codigoCliente(int Clientes_codigoCliente) {
        this.Clientes_codigoCliente = Clientes_codigoCliente;
    }

    public int getEmpleados_codigoEmpleado() {
        return Empleados_codigoEmpleado;
    }

    public void setEmpleados_codigoEmpleado(int Empleados_codigoEmpleado) {
        this.Empleados_codigoEmpleado = Empleados_codigoEmpleado;
    }
}

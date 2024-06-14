
package org.mariorodriguez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.mariorodriguez.bean.Clientes;
import org.mariorodriguez.bean.Empleados;
import org.mariorodriguez.bean.Factura;
import org.mariorodriguez.db.Conexion;
import org.mariorodriguez.system.Main;

public class FacturaController implements Initializable{
    private Main escenarioPrincipalFactura;
    
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Factura> listarFacturas;
    private ObservableList<Clientes> listarClientes;
    private ObservableList<Empleados> listarEmpleados;
    
    /*Botones*/
    @FXML private Button btnRegresar;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    
    /*Imagenes*/
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReportes;
    
    /*Textos*/
    @FXML private TextField txtEstado;
    @FXML private TextField txtFecha;
    
    /*Combo box*/
    @FXML private ComboBox cmbIdClientes;
    @FXML private ComboBox cmbIdEmpleados;
    
    /*Tabla*/
    @FXML private TableView tblFactura;
    
    /*Columnas*/
    @FXML private TableColumn colID;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn colTotal;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colIdClientes;
    @FXML private TableColumn colIdEmpleados;
    
    public FacturaController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarTxt();
        cmbIdClientes.setItems(getClientes());
        cmbIdEmpleados.setItems(getEmpleados());
        
        if(listarFacturas.isEmpty()){
            reinicioId();
        }
    }
    
    public void cargarDatos(){
        tblFactura.setItems(getFactura());
        colID.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("numeroFactura"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Factura, String>("estado"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Factura, Double>("totalFactura"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Factura, String>("fechaFactura"));
        colIdClientes.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("Clientes_codigoCliente"));
        colIdEmpleados.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("Empleados_codigoEmpleado"));
    }

    public void selecionarElemento(){
        txtEstado.setText(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getEstado());
        txtFecha.setText(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getFechaFactura());        
        cmbIdClientes.getSelectionModel().select(buscarC(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getClientes_codigoCliente()));
        cmbIdEmpleados.getSelectionModel().select(buscarE(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getEmpleados_codigoEmpleado()));
    }
    
    public Clientes buscarC(int codigoCliente){
        Clientes resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarClientes(?)}");
            procedimiento.setInt(1, codigoCliente);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Clientes(registro.getInt("codigoCliente"),
                                              registro.getString("NITcliente"),
                                              registro.getString("nombreCliente"),
                                              registro.getString("apellidoCliente"),
                                              registro.getString("direccionCliente"),
                                              registro.getString("telefonoCliente"),
                                              registro.getString("correoCliente"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Empleados buscarE(int codigoEmpleado){
        Empleados resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Empleados(registro.getInt("codigoEmpleado"),
                                              registro.getString("nombreEmpleado"),
                                              registro.getString("apellidoEmpleado"),
                                              registro.getDouble("sueldo"),
                                              registro.getString("direccionEmpleado"),
                                              registro.getString("turno"),
                                              registro.getInt("CargoEmpleado_codigoCargoEmpleado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Factura> getFactura(){
        ArrayList<Factura> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarFacturas()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                Lista.add(new Factura (resultado.getInt("numeroFactura"),
                                        resultado.getString("estado"),
                                        resultado.getDouble("totalFactura"),
                                        resultado.getString("fechaFactura"),
                                        resultado.getInt("Clientes_codigoCliente"),
                                        resultado.getInt("Empleados_codigoEmpleado")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarFacturas = FXCollections.observableArrayList(Lista);
    }
    
    public ObservableList<Clientes> getClientes(){
        ArrayList<Clientes> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new Clientes (resultado.getInt("codigoCliente"),
                                        resultado.getString("NITcliente"),
                                        resultado.getString("nombreCliente"),
                                        resultado.getString("apellidoCliente"),
                                        resultado.getString("direccionCliente"),
                                        resultado.getString("telefonoCliente"),
                                        resultado.getString("correoCliente")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarClientes = FXCollections.observableArrayList(Lista);
    }
    
    public ObservableList<Empleados> getEmpleados(){
        ArrayList<Empleados> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                Lista.add(new Empleados (resultado.getInt("codigoEmpleado"),
                                        resultado.getString("nombreEmpleado"),
                                        resultado.getString("apellidoEmpleado"),
                                        resultado.getDouble("sueldo"),
                                        resultado.getString("direccionEmpleado"),
                                        resultado.getString("turno"),
                                        resultado.getInt("CargoEmpleado_codigoCargoEmpleado")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarEmpleados = FXCollections.observableArrayList(Lista);
    }
    
    public void agregarFactura(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarTxt();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReportes.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/guardar-el-archivo.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/error.png"));
                break;
            case ACTUALIZAR:
                guardarDatos();
                desactivarTxt();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-usuario.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/quitar-usuario.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void guardarDatos(){
        Factura registro = new Factura();

        registro.setEstado(txtEstado.getText());
        registro.setFechaFactura(txtFecha.getText());
        registro.setClientes_codigoCliente(((Clientes)cmbIdClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
        registro.setEmpleados_codigoEmpleado(((Empleados)cmbIdEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
       
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarFactura(?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getEstado());
            procedimiento.setString(2, registro.getFechaFactura());
            procedimiento.setInt(3, registro.getClientes_codigoCliente());
            procedimiento.setInt(4, registro.getEmpleados_codigoEmpleado());
            procedimiento.execute();
            
            listarFacturas.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarFactura(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-usuario.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/quitar-usuario.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                desactivarTxt();
                break;
            default:
                if(tblFactura.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confimar si desea eliminar el registro", "Eliminar Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarFactura(?)}");
                            procedimiento.setInt(1, ((Factura)tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
                            procedimiento.execute();
                            
                            listarFacturas.remove(tblFactura.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(listarEmpleados.isEmpty()){
                        reinicioId();
                    }
                    
                    cargarDatos();
                    limpiarControles();
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento");
                }
                break;
            
        }
    }
    
    public void editarFactura(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblFactura.getSelectionModel().getSelectedItem() !=null){
                    activarTxt();
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/mariorodriguez/images/editar.png"));
                    imgReportes.setImage(new Image("/org/mariorodriguez/images/error.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar algun elemento");
                }
                break;
            case ACTUALIZAR:
                actualizarDatos();
                btnEditar.setText("Editar");
                btnReportes.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/usuario editar.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                desactivarTxt();
                break;
        }
    }
    
    public void reportes(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarTxt();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/usuario editar.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizarDatos(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarFactura(?, ?, ?, ?, ?)}");
            Factura registro = (Factura)tblFactura.getSelectionModel().getSelectedItem();
            
            registro.setEstado(txtEstado.getText());
            registro.setFechaFactura(txtFecha.getText());
            registro.setClientes_codigoCliente(((Clientes)cmbIdClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
            registro.setEmpleados_codigoEmpleado(((Empleados)cmbIdEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
       

            procedimiento.setInt(1, registro.getNumeroFactura());
            procedimiento.setString(2, registro.getEstado());
            procedimiento.setString(3, registro.getFechaFactura());
            procedimiento.setInt(4, registro.getClientes_codigoCliente());
            procedimiento.setInt(5, registro.getEmpleados_codigoEmpleado());
            procedimiento.execute();

            listarFacturas.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reinicioId(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_reinicioIdFactura()}");
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarTxt(){
        txtEstado.setDisable(false);
        txtFecha.setDisable(false);
        cmbIdClientes.setDisable(false);
        cmbIdEmpleados.setDisable(false);
    }
    
    public void desactivarTxt(){
        txtEstado.setDisable(true);
        txtFecha.setDisable(true);
        cmbIdClientes.setDisable(true);
        cmbIdEmpleados.setDisable(true);
    }
    
    public void limpiarControles(){
        txtEstado.clear();
        txtFecha.clear();
        cmbIdClientes.getSelectionModel().getSelectedItem();
        cmbIdEmpleados.getSelectionModel().getSelectedItem();
    }
    
    public Main getEscenarioPrincipalFactura() {
        return escenarioPrincipalFactura;
    }

    public void setEscenarioPrincipalFactura(Main escenarioPrincipalFactura) {
        this.escenarioPrincipalFactura = escenarioPrincipalFactura;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipalFactura.menuPrincipalView();
        }
    } 
}

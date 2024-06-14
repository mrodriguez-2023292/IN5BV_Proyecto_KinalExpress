
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
import org.mariorodriguez.bean.Cargos;
import org.mariorodriguez.bean.DetalleFactura;
import org.mariorodriguez.bean.Empleados;
import org.mariorodriguez.bean.Factura;
import org.mariorodriguez.bean.Productos;
import org.mariorodriguez.db.Conexion;
import org.mariorodriguez.system.Main;

public class DetalleFacturaController implements Initializable{
    private Main escenarioPrincipalDetalleFactura;
    
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<DetalleFactura> listarDetalleFactura;
    private ObservableList<Factura> listarFacturas;
    private ObservableList<Productos> listarProductos;
    
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
    @FXML private TextField txtCantidad;
    
    /*Combo box*/
    @FXML private ComboBox cmbIdFactura;
    @FXML private ComboBox cmbIdProductos;
    
    /*Tabla*/
    @FXML private TableView tblDetalleFactura;
    
    /*Columnas*/
    @FXML private TableColumn colID;
    @FXML private TableColumn colPrecioU;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colIdFactura;
    @FXML private TableColumn colIdProductos;
    
    public DetalleFacturaController() {
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarTxt();
        cmbIdFactura.setItems(getFactura());
        cmbIdProductos.setItems(getProductos());
        
        if(listarDetalleFactura.isEmpty()){
            reinicioId();
        }
    }
    
    public void cargarDatos(){
        tblDetalleFactura.setItems(getDetalleFactura());
        colID.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleado"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("nombreEmpleado"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("apellidoEmpleado"));
        colIdFactura.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("CargoEmpleado_codigoCargoEmpleado"));
        colIdProductos.setCellValueFactory(new PropertyValueFactory<Empleados, String>("CargoEmpleado_codigoCargoEmpleado"));
    }

    public void selecionarElemento(){
        txtCantidad.setText(String.valueOf(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbIdFactura.getSelectionModel().select(buscarF(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getFactura_numeroFactura()));
        cmbIdProductos.getSelectionModel().select(buscarP(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
    }
    
    public Factura buscarF(int numeroFactura){
        Factura resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarFactura(?)}");
            procedimiento.setInt(1, numeroFactura);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Factura(registro.getInt("numeroFactura"),
                                              registro.getString("estado"),
                                              registro.getDouble("totalFactura"),
                                              registro.getString("fechaFactura"),
                                              registro.getInt("Clientes_codigoCliente"),
                                              registro.getInt("Empleados_codigoEmpleado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Productos buscarP(String codigoProducto){
        Productos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
            procedimiento.setString(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Productos(registro.getString("codigoProducto"),
                                              registro.getString("descripcionProducto"),
                                              registro.getDouble("precioUnitario"),
                                              registro.getDouble("precioDocena"),
                                              registro.getDouble("precioMayor"),
                                              registro.getInt("existencia"),
                                              registro.getInt("TipoProducto_codigoTipoProducto"),
                                              registro.getInt("Proveedores_codigoProveedor"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<DetalleFactura> getDetalleFactura(){
        ArrayList<DetalleFactura> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarDetalleFacturas()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                Lista.add(new DetalleFactura (resultado.getInt("codigoDetalleFactura"),
                                        resultado.getDouble("precioUnitario"),
                                        resultado.getInt("cantidad"),
                                        resultado.getInt("Factura_numeroFactura"),
                                        resultado.getString("Productos_codigoProducto")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarDetalleFactura = FXCollections.observableArrayList(Lista);
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
    
    public ObservableList<Productos> getProductos(){
        ArrayList<Productos> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                Lista.add(new Productos (resultado.getString("codigoProducto"),
                                        resultado.getString("descripcionProducto"),
                                        resultado.getDouble("precioUnitario"),
                                        resultado.getDouble("precioDocena"),
                                        resultado.getDouble("precioMayor"),
                                        resultado.getInt("existencia"),
                                        resultado.getInt("TipoProducto_codigoTipoProducto"),
                                        resultado.getInt("Proveedores_codigoProveedor")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarProductos = FXCollections.observableArrayList(Lista);
    }
    
    public void agregarDetalleCompra(){
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
        DetalleFactura registro = new DetalleFactura();

        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setFactura_numeroFactura(((Factura)cmbIdFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
        registro.setProductos_codigoProducto(((Productos)cmbIdProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
       
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarDetalleFactura(?, ?, ?)}");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setInt(2, registro.getFactura_numeroFactura());
            procedimiento.setString(3, registro.getProductos_codigoProducto());
            procedimiento.execute();
            
            listarDetalleFactura.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarDetalleFactura(){
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
                if(tblDetalleFactura.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confimar si desea eliminar el registro", "Eliminar Detalle factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarDetalleFactura(?)}");
                            procedimiento.setInt(1, ((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
                            procedimiento.execute();
                            
                            listarFacturas.remove(tblDetalleFactura.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(listarDetalleFactura.isEmpty()){
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
                if(tblDetalleFactura.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarDetalleFactura(?, ?, ?, ?)}");
            DetalleFactura registro = (DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem();
            
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setFactura_numeroFactura(((Factura)cmbIdFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
            registro.setProductos_codigoProducto(((Productos)cmbIdProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());

            procedimiento.setInt(1, registro.getCodigoDetalleFactura());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setInt(3, registro.getFactura_numeroFactura());
            procedimiento.setString(4, registro.getProductos_codigoProducto());
            procedimiento.execute();

            listarDetalleFactura.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reinicioId(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_reinicioIdDetalleFactura()}");
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarTxt(){
        txtCantidad.setDisable(false);
        cmbIdFactura.setDisable(false);
        cmbIdProductos.setDisable(false);
    }
    
    public void desactivarTxt(){
        txtCantidad.setDisable(true);
        cmbIdFactura.setDisable(true);
        cmbIdProductos.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCantidad.clear();
        cmbIdFactura.getSelectionModel().getSelectedItem();
        cmbIdProductos.getSelectionModel().getSelectedItem();
    }

    public Main getEscenarioPrincipalDetalleFactura() {
        return escenarioPrincipalDetalleFactura;
    }

    public void setEscenarioPrincipalDetalleFactura(Main escenarioPrincipalDetalleFactura) {
        this.escenarioPrincipalDetalleFactura = escenarioPrincipalDetalleFactura;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipalDetalleFactura.menuPrincipalView();
        }
    } 
}

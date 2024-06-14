
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
import org.mariorodriguez.bean.Compras;
import org.mariorodriguez.bean.DetalleCompra;
import org.mariorodriguez.bean.Productos;
import org.mariorodriguez.db.Conexion;
import org.mariorodriguez.system.Main;

public class DetalleCompraController implements Initializable{
    private Main escenarioPrincipalDetalleCompra;
    
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<DetalleCompra> listarDetalleCompra;
    private ObservableList<Productos> listarProductos;
    private ObservableList<Compras> listarCompras;
    
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
    @FXML private TextField txtCostoU;
    @FXML private TextField txtCantidad;
    
    /*Combo box*/
    @FXML private ComboBox cmbIdProductos;
    @FXML private ComboBox cmbIdCompras;
    
    /*Tabla*/
    @FXML private TableView tblDetalleCompra;
    
    /*Columnas*/
    @FXML private TableColumn colID;
    @FXML private TableColumn colCostoU;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colIdProductos;
    @FXML private TableColumn colIdCompras;
    
    public DetalleCompraController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarTxt();
        cmbIdProductos.setItems(getProductos());
        cmbIdCompras.setItems(getCompras());
        
        if(listarDetalleCompra.isEmpty()){
            reinicioId();
        }
    }
    
    public void cargarDatos(){
        tblDetalleCompra.setItems(getDetalleCompra());
        colID.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("codigoDetalleCompra"));
        colCostoU.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Double>("costoUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("cantidad"));
        colIdProductos.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("Productos_codigoProducto"));
        colIdCompras.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("Compras_numeroDocumento"));
    }

    public void selecionarElemento(){
        txtCostoU.setText(String.valueOf(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCostoUnitario()));
        txtCantidad.setText(String.valueOf(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbIdProductos.getSelectionModel().select(buscarP(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
        cmbIdCompras.getSelectionModel().select(buscarC(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCompras_numeroDocumento()));
    }
    
    public Productos buscarP(String codigoProducto){
        Productos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarDetalleCompra(?)}");
            procedimiento.setString(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Productos(registro.getString("estado"),
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
    
    public Compras buscarC(int numeroDocumento){
        Compras resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCompras(?)}");
            procedimiento.setInt(1, numeroDocumento);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Compras(registro.getInt("numeroDocumento"),
                                              registro.getString("fechaDocumento"),
                                              registro.getString("descripcion"),
                                              registro.getDouble("totalDocumento"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<DetalleCompra> getDetalleCompra(){
        ArrayList<DetalleCompra> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarDetalleCompra()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                Lista.add(new DetalleCompra (resultado.getInt("codigoDetalleCompra"),
                                        resultado.getDouble("costoUnitario"),
                                        resultado.getInt("cantidad"),
                                        resultado.getString("Productos_codigoProducto"),
                                        resultado.getInt("Compras_numeroDocumento")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarDetalleCompra = FXCollections.observableArrayList(Lista);
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
    
    public ObservableList<Compras> getCompras(){
        ArrayList<Compras> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCompras()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                Lista.add(new Compras (resultado.getInt("numeroDocumento"),
                                        resultado.getString("fechaDocumento"),
                                        resultado.getString("descripcion"),
                                        resultado.getDouble("totalDocumento")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarCompras = FXCollections.observableArrayList(Lista);
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
        DetalleCompra registro = new DetalleCompra();

        registro.setCostoUnitario(Double.parseDouble(txtCostoU.getText()));
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setProductos_codigoProducto(((Productos)cmbIdProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
        registro.setCompras_numeroDocumento(((Compras)cmbIdCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
       
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarDetalleCompra(?, ?, ?, ?)}");
            procedimiento.setDouble(1, registro.getCostoUnitario());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getProductos_codigoProducto());
            procedimiento.setInt(4, registro.getCompras_numeroDocumento());
            procedimiento.execute();
            
            listarDetalleCompra.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarDetalleCompra(){
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
                if(tblDetalleCompra.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confimar si desea eliminar el registro", "Eliminar Detalle compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarDetalleCompra(?)}");
                            procedimiento.setInt(1, ((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
                            procedimiento.execute();
                            
                            listarDetalleCompra.remove(tblDetalleCompra.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(listarDetalleCompra.isEmpty()){
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
    
    public void editarDetalleCompra(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblDetalleCompra.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarDetalleCompra(?, ?, ?, ?, ?)}");
            DetalleCompra registro = (DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem();
            
            registro.setCostoUnitario(Double.parseDouble(txtCostoU.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setProductos_codigoProducto(((Productos)cmbIdProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
            registro.setCompras_numeroDocumento(((Compras)cmbIdCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());

            procedimiento.setInt(1, registro.getCodigoDetalleCompra());
            procedimiento.setDouble(2, registro.getCostoUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setString(4, registro.getProductos_codigoProducto());
            procedimiento.setInt(5, registro.getCompras_numeroDocumento());
            procedimiento.execute();

            listarDetalleCompra.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reinicioId(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_reinicioIdDetalleCompra()}");
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarTxt(){
        txtCostoU.setDisable(false);
        txtCantidad.setDisable(false);
        cmbIdProductos.setDisable(false);
        cmbIdCompras.setDisable(false);
    }
    
    public void desactivarTxt(){
        txtCostoU.setDisable(true);
        txtCantidad.setDisable(true);
        cmbIdProductos.setDisable(true);
        cmbIdCompras.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCostoU.clear();
        txtCantidad.clear();
        cmbIdProductos.getSelectionModel().getSelectedItem();
        cmbIdCompras.getSelectionModel().getSelectedItem();
    }
    
    public Main getEscenarioPrincipalDetalleCompra() {
        return escenarioPrincipalDetalleCompra;
    }

    public void setEscenarioPrincipalDetalleCompra(Main escenarioPrincipalDetalleCompra) {
        this.escenarioPrincipalDetalleCompra = escenarioPrincipalDetalleCompra;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipalDetalleCompra.menuPrincipalView();
        }
    } 
}

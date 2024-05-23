
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
import org.mariorodriguez.bean.Productos;
import org.mariorodriguez.bean.Proveedores;
import org.mariorodriguez.bean.TipoProductos;
import org.mariorodriguez.db.Conexion;
import org.mariorodriguez.system.Main;

public class ProductosController implements Initializable{
    private Main escenarioPrincipalProductos;
    
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Productos> listarProductos;
    private ObservableList<Proveedores> listarProveedores;
    private ObservableList<TipoProductos> listarTipoProductos;
    
    /*Botones*/
    @FXML private Button btnRegresarPro;
    @FXML private Button btnAgregarPro;
    @FXML private Button btnEliminarPro;
    @FXML private Button btnEditarPro;
    @FXML private Button btnReportesPro;
    
    /*Imagenes*/
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReportes;
    
    /*Textos*/
    @FXML private TextField txtCodPro;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtExistencia;
    @FXML private TextField txtPrecioU;
    @FXML private TextField txtPrecioD;
    @FXML private TextField txtPrecioM;
    
    /*Combo box*/
    @FXML private ComboBox cmdIDTP;
    @FXML private ComboBox cmbProveedores;
    
    /*Tabla*/
    @FXML private TableView tblProductos;
    
    /*Columnas*/
    @FXML private TableColumn colCodPro;
    @FXML private TableColumn colDes;
    @FXML private TableColumn colPrecioU;
    @FXML private TableColumn colPrecioD;
    @FXML private TableColumn colPrecioM;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colIdTipP;
    @FXML private TableColumn colIdProve;
    
    public ProductosController() {
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarTxtPro();
        cmdIDTP.setItems(getTipoProductos());
        cmbProveedores.setItems(getProveedores());
    }
    
    public void cargarDatos(){
        tblProductos.setItems(getProductos());
        colCodPro.setCellValueFactory(new PropertyValueFactory<Productos, String>("codigoProducto"));
        colDes.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioUnitario"));
        colPrecioD.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioDocena"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioMayor"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("existencia"));
        colIdTipP.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("TipoProducto_codigoTipoProducto"));
        colIdProve.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("Proveedores_codigoProveedor"));
    }
    
    public void selecionarElemento(){
        txtCodPro.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
        txtDescripcion.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
        txtPrecioU.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtPrecioD.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
        txtPrecioM.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
        txtExistencia.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
        cmdIDTP.getSelectionModel().select(buscarTP(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getTipoProducto_codigoTipoProducto()));
    }
    
    public TipoProductos buscarTP(int codigoTipoProducto){
        TipoProductos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTipoProductos(?)}");
            procedimiento.setInt(1, codigoTipoProducto);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new TipoProductos(registro.getInt("codigoTipoProducto"),
                                              registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
    
    public ObservableList<TipoProductos> getTipoProductos(){
        ArrayList<TipoProductos> ListaTP = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTipoProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                ListaTP.add(new TipoProductos (resultado.getInt("codigoTipoProducto"),
                                        resultado.getString("descripcion")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarTipoProductos = FXCollections.observableArrayList(ListaTP);
    }
    
    public ObservableList<Proveedores> getProveedores(){
        ArrayList<Proveedores> ListaPro = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                ListaPro.add(new Proveedores (resultado.getInt("codigoProveedor"),
                                        resultado.getString("NITproveedor"),
                                        resultado.getString("nombreProveedor"),
                                        resultado.getString("apellidoProveedor"),
                                        resultado.getString("direccionProveedor"),
                                        resultado.getString("razonSocial"),
                                        resultado.getString("contactoPrincipal"),
                                        resultado.getString("paginaWeb")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarProveedores = FXCollections.observableArrayList(ListaPro);
    }
    
    public void agregarProducto(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarTxtPro();
                btnAgregarPro.setText("Guardar");
                btnEliminarPro.setText("Cancelar");
                btnEditarPro.setDisable(true);
                btnReportesPro.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/guardar-el-archivo.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/error.png"));
                break;
            case ACTUALIZAR:
                guardarProducto();
                desactivarTxtPro();
                limpiarControles();
                btnAgregarPro.setText("Agregar");
                btnEliminarPro.setText("Eliminar");
                btnEditarPro.setDisable(false);
                btnReportesPro.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-usuario.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/quitar-usuario.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardarProducto(){
        Productos registro = new Productos();

        registro.setCodigoProducto(txtCodPro.getText());
        registro.setProveedores_codigoProveedor(((Proveedores)cmbProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setTipoProducto_codigoTipoProducto(((TipoProductos)cmdIDTP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
        registro.setDescripcionProducto(txtDescripcion.getText());
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
        registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
        registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
        registro.setExistencia(Integer.parseInt(txtExistencia.getText()));

        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarProductos(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setInt(6, registro.getExistencia());
            procedimiento.setInt(7, registro.getTipoProducto_codigoTipoProducto());
            procedimiento.setInt(8, registro.getProveedores_codigoProveedor());
            procedimiento.execute();
            
            listarProductos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarProducto(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                limpiarControles();
                btnAgregarPro.setText("Agregar");
                btnEliminarPro.setText("Eliminar");
                btnEditarPro.setDisable(false);
                btnReportesPro.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-usuario.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/quitar-usuario.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                desactivarTxtPro();
                break;
            default:
                if(tblProductos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confimar si desea eliminar el registro", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                            procedimiento.setString(1, ((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            
                            listarProveedores.remove(tblProductos.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    cargarDatos();
                    limpiarControles();
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento");
                }
                break;
            
        }
    }
    
    public void editarProducto(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblProductos.getSelectionModel().getSelectedItem() !=null){
                    activarTxtPro();
                    btnEditarPro.setText("Actualizar");
                    btnReportesPro.setText("Cancelar");
                    btnAgregarPro.setDisable(true);
                    btnEliminarPro.setDisable(true);
                    imgEditar.setImage(new Image("/org/mariorodriguez/images/editar.png"));
                    imgReportes.setImage(new Image("/org/mariorodriguez/images/error.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar algun elemento");
                }
                break;
            case ACTUALIZAR:
                actualizarProducto();
                btnEditarPro.setText("Editar");
                btnReportesPro.setText("Reporte");
                btnAgregarPro.setDisable(false);
                btnEliminarPro.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/usuario editar.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                desactivarTxtPro();
                break;
        }
    }
    
    public void reportesProductos(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarTxtPro();
                limpiarControles();
                btnEditarPro.setText("Editar");
                btnReportesPro.setText("Reportes");
                btnAgregarPro.setDisable(false);
                btnEliminarPro.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/usuario editar.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizarProducto(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarProducto(?, ?, ?, ?, ?, ?, ?, ?)}");
            Productos registro = (Productos)tblProductos.getSelectionModel().getSelectedItem();
            
            registro.setCodigoProducto(txtCodPro.getText());
            registro.setProveedores_codigoProveedor(((Proveedores)cmbProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setTipoProducto_codigoTipoProducto(((TipoProductos)cmdIDTP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
            registro.setDescripcionProducto(txtDescripcion.getText());
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
            registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setInt(6, registro.getExistencia());
            procedimiento.setInt(7, registro.getTipoProducto_codigoTipoProducto());
            procedimiento.setInt(8, registro.getProveedores_codigoProveedor());
            procedimiento.execute();

            listarProductos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarTxtPro(){
        txtCodPro.setDisable(false);
        txtDescripcion.setDisable(false);
        txtExistencia.setDisable(false);
        txtPrecioU.setDisable(false);
        txtPrecioD.setDisable(false);
        txtPrecioM.setDisable(false);
        cmdIDTP.setDisable(false);
        cmbProveedores.setDisable(false);
    }
    
    public void desactivarTxtPro(){
        txtCodPro.setDisable(true);
        txtDescripcion.setDisable(true);
        txtExistencia.setDisable(true);
        txtPrecioU.setDisable(true);
        txtPrecioD.setDisable(true);
        txtPrecioM.setDisable(true);
        cmdIDTP.setDisable(true);
        cmbProveedores.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodPro.clear();
        txtDescripcion.clear();
        txtExistencia.clear();
        txtPrecioU.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        cmdIDTP.getSelectionModel().getSelectedItem();
        cmbProveedores.getSelectionModel().getSelectedItem();
    }

    public Main getEscenarioPrincipalProductos() {
        return escenarioPrincipalProductos;
    }

    public void setEscenarioPrincipalProductos(Main escenarioPrincipalProductos) {
        this.escenarioPrincipalProductos = escenarioPrincipalProductos;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresarPro){
            escenarioPrincipalProductos.menuPrincipalView();
        }
    }  
}

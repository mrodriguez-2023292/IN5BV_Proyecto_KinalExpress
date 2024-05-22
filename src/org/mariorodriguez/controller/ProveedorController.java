
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.mariorodriguez.bean.Proveedores;
import org.mariorodriguez.db.Conexion;
import org.mariorodriguez.system.Main;

public class ProveedorController implements Initializable{
    private Main escenarioPrincipalProveedores;
    
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Proveedores> listarProveedores;
    
    /*Botones*/
    @FXML private Button btnRegresarP;
    @FXML private Button btnAgregarP;
    @FXML private Button btnEliminarP;
    @FXML private Button btnEditarP;
    @FXML private Button btnReportesP;
    
    /*Imagenes*/
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReportes;
    
    /*Textos*/
    @FXML private TextField txtNombreP;
    @FXML private TextField txtApellidoP;
    @FXML private TextField txtDireccionP;
    @FXML private TextField txtRazonSP;
    @FXML private TextField txtContactoPP;
    @FXML private TextField txtPaginaWP;
    @FXML private TextField txtNITP;
    
    /*Tabla*/
    @FXML private TableView tblProveedores;
    
    /*Columnas*/
    @FXML private TableColumn colCodigoP;
    @FXML private TableColumn colNITP;
    @FXML private TableColumn colNombreP;
    @FXML private TableColumn colApellidoP;
    @FXML private TableColumn colDireccionP;
    @FXML private TableColumn colRazonSP;
    @FXML private TableColumn colContactoPP;
    @FXML private TableColumn colPaginaWP;
    
    public ProveedorController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarTxtP();
        
        if(listarProveedores.isEmpty()){
            reinicioProveedores();
        }
    }
    
    public void cargarDatos(){
        tblProveedores.setItems(getProveedores());
        colCodigoP.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("codigoProveedor"));
        colNITP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("NITproveedor"));
        colNombreP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nombreProveedor"));
        colApellidoP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("apellidoProveedor"));
        colDireccionP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccionProveedor"));
        colRazonSP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("razonSocial"));
        colContactoPP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("contactoPrincipal"));
        colPaginaWP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("paginaWeb"));
    }
    
    public void selecionarElemento(){
        txtNITP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNITproveedor());
        txtNombreP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNombreProveedor());
        txtApellidoP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getApellidoProveedor());
        txtDireccionP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor());
        txtRazonSP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial());
        txtContactoPP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal());
        txtPaginaWP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWeb());
    }
    
    public ObservableList<Proveedores> getProveedores(){
        ArrayList<Proveedores> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                Lista.add(new Proveedores (resultado.getInt("codigoProveedor"),
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
        return listarProveedores = FXCollections.observableArrayList(Lista);
    }
    
    public void agregarProveedor(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarTxtP();
                btnAgregarP.setText("Guardar");
                btnEliminarP.setText("Cancelar");
                btnEditarP.setDisable(true);
                btnReportesP.setDisable(true);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/guardar-el-archivo.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/error.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardarProveedor();
                desactivarTxtP();
                limpiarControles();
                btnAgregarP.setText("Agregar");
                btnEliminarP.setText("Eliminar");
                btnEditarP.setDisable(false);
                btnReportesP.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-usuario.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/quitar-usuario.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardarProveedor(){
        Proveedores registro = new Proveedores();
        
        registro.setNITproveedor(txtNITP.getText());
        registro.setNombreProveedor(txtNombreP.getText());
        registro.setApellidoProveedor(txtApellidoP.getText());
        registro.setDireccionProveedor(txtDireccionP.getText());
        registro.setRazonSocial(txtRazonSP.getText());
        registro.setContactoPrincipal(txtContactoPP.getText());
        registro.setPaginaWeb(txtPaginaWP.getText());

        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarProveedores(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getNITproveedor());
            procedimiento.setString(2, registro.getNombreProveedor());
            procedimiento.setString(3, registro.getApellidoProveedor());
            procedimiento.setString(4, registro.getDireccionProveedor());
            procedimiento.setString(5, registro.getRazonSocial());
            procedimiento.setString(6, registro.getContactoPrincipal());
            procedimiento.setString(7, registro.getPaginaWeb());
            procedimiento.execute();
            
            listarProveedores.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarProveedor(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                limpiarControles();
                btnAgregarP.setText("Agregar");
                btnEliminarP.setText("Eliminar");
                btnEditarP.setDisable(false);
                btnReportesP.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-usuario.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/quitar-usuario.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                desactivarTxtP();
                break;
            default:
                if(tblProveedores.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confimar si desea eliminar el registro", "Eliminar Proveedores", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProveedores(?)}");
                            procedimiento.setInt(1, ((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            
                            listarProveedores.remove(tblProveedores.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                    if(listarProveedores.isEmpty()){
                        reinicioProveedores();
                    }
                    limpiarControles();
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento");
                }
                break;
            
        }
    }
    
    public void editarProveedor(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblProveedores.getSelectionModel().getSelectedItem() !=null){
                    activarTxtP();
                    btnEditarP.setText("Actualizar");
                    btnReportesP.setText("Cancelar");
                    btnAgregarP.setDisable(true);
                    btnEliminarP.setDisable(true);
                    imgEditar.setImage(new Image("/org/mariorodriguez/images/editar.png"));
                    imgReportes.setImage(new Image("/org/mariorodriguez/images/error.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar algun elemento");
                }
                break;
            case ACTUALIZAR:
                actualizarProveedor();
                btnEditarP.setText("Editar");
                btnReportesP.setText("Reporte");
                btnAgregarP.setDisable(false);
                btnEliminarP.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/usuario editar.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                desactivarTxtP();
                break;
        }
    }
    
    public void reportesProveedores(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarTxtP();
                limpiarControles();
                btnEditarP.setText("Editar");
                btnReportesP.setText("Reportes");
                btnAgregarP.setDisable(false);
                btnEliminarP.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/usuario editar.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizarProveedor(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarProveedores(?, ?, ?, ?, ?, ?, ?, ?)}");
            Proveedores registro = (Proveedores)tblProveedores.getSelectionModel().getSelectedItem();
            
            registro.setNITproveedor(txtNITP.getText());
            registro.setNombreProveedor(txtNombreP.getText());
            registro.setApellidoProveedor(txtApellidoP.getText());
            registro.setDireccionProveedor(txtDireccionP.getText());
            registro.setRazonSocial(txtRazonSP.getText());
            registro.setContactoPrincipal(txtContactoPP.getText());
            registro.setPaginaWeb(txtPaginaWP.getText());
            
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITproveedor());
            procedimiento.setString(3, registro.getNombreProveedor());
            procedimiento.setString(4, registro.getApellidoProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWeb());
            procedimiento.execute();

            listarProveedores.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }        
            
    public void reinicioProveedores(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_reinicioIdProveedores()}");
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarTxtP(){
        txtNombreP.setDisable(false);
        txtApellidoP.setDisable(false);
        txtDireccionP.setDisable(false);
        txtRazonSP.setDisable(false);
        txtContactoPP.setDisable(false);
        txtPaginaWP.setDisable(false); 
        txtNITP.setDisable(false);
    }
    
    public void desactivarTxtP(){
        txtNombreP.setDisable(true);
        txtApellidoP.setDisable(true);
        txtDireccionP.setDisable(true);
        txtRazonSP.setDisable(true);
        txtContactoPP.setDisable(true);
        txtPaginaWP.setDisable(true); 
        txtNITP.setDisable(true);
    }
    
    public void limpiarControles(){
        txtNombreP.clear();
        txtApellidoP.clear();
        txtDireccionP.clear();
        txtRazonSP.clear();
        txtContactoPP.clear();
        txtPaginaWP.clear();
        txtNITP.clear();
    }
    
    public Main getEscenarioPrincipalProveedores() {
        return escenarioPrincipalProveedores;
    }

    public void setEscenarioPrincipalProveedores(Main escenarioPrincipalProveedores) {
        this.escenarioPrincipalProveedores = escenarioPrincipalProveedores;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresarP){
            escenarioPrincipalProveedores.menuPrincipalView();
        }
    }
}

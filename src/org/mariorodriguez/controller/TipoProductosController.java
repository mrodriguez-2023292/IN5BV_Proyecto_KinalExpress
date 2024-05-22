
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
import org.mariorodriguez.bean.TipoProductos;
import org.mariorodriguez.db.Conexion;
import org.mariorodriguez.system.Main;

public class TipoProductosController implements Initializable {
    private Main escenarioPrincipalTipoProductos;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<TipoProductos> listarTipoProductos;
    
    /*Botones*/
    @FXML private Button btnRegresarTP;
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
    @FXML private TextField txtDescripcionTP;
    
    /*Tabla*/
    @FXML private TableView tblTipoProductos;
    
    /*Columnas*/
    @FXML private TableColumn colCodTP;
    @FXML private TableColumn colDes;
    
    public TipoProductosController() {
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        desactivarTxtCom();
        
        if(listarTipoProductos.isEmpty()){
            reinicioTipoProductos();
        }
    }  

    public void cargarDatos(){
        tblTipoProductos.setItems(getTipoProductos());
        colCodTP.setCellValueFactory(new PropertyValueFactory<TipoProductos, Integer>("codigoTipoProducto"));
        colDes.setCellValueFactory(new PropertyValueFactory<TipoProductos, String>("descripcion"));
    }
    
    public void selecionarElemento(){
        txtDescripcionTP.setText(((TipoProductos)tblTipoProductos.getSelectionModel().getSelectedItem()).getDescripcion());
    }
    
    public ObservableList<TipoProductos> getTipoProductos(){
        ArrayList<TipoProductos> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTipoProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                Lista.add(new TipoProductos (resultado.getInt("codigoTipoProducto"),
                                        resultado.getString("descripcion")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarTipoProductos = FXCollections.observableArrayList(Lista);
    }
    
    public void agregarTipoProductos(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarTxtCom();
                btnAgregarPro.setText("Guardar");
                btnEliminarPro.setText("Cancelar");
                btnEditarPro.setDisable(true);
                btnReportesPro.setDisable(true);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/guardar-el-archivo.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/error.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardarTipoProductos();
                desactivarTxtCom();
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
    
    public void guardarTipoProductos(){
        TipoProductos registro = new TipoProductos();
        
        registro.setDescripcion(txtDescripcionTP.getText());

        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTipoProductos(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            
            listarTipoProductos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarTipoProductos(){
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
                desactivarTxtCom();
                break;
            default:
                if(tblTipoProductos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confimar si desea eliminar el registro", "Eliminar tipo de productos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTipoProductos(?)}");
                            procedimiento.setInt(1, ((TipoProductos)tblTipoProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            procedimiento.execute();
                            
                            listarTipoProductos.remove(tblTipoProductos.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                    if(listarTipoProductos.isEmpty()){
                        reinicioTipoProductos();
                    }
                    limpiarControles();
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento");
                }
                break;
            
        }
    }
    
    public void editarTipoProductos(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblTipoProductos.getSelectionModel().getSelectedItem() !=null){
                    activarTxtCom();
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
                actualizarTipoProductos();
                btnEditarPro.setText("Editar");
                btnReportesPro.setText("Reporte");
                btnAgregarPro.setDisable(false);
                btnEliminarPro.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/usuario editar.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                desactivarTxtCom();
                break;
        }
    }
    
    public void reportesTipoProductos(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarTxtCom();
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
    
    public void actualizarTipoProductos(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTipoProductos(?, ?)}");
            TipoProductos registro = (TipoProductos)tblTipoProductos.getSelectionModel().getSelectedItem();
            
            registro.setDescripcion(txtDescripcionTP.getText());
            
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();

            listarTipoProductos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }        
            
    public void reinicioTipoProductos(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_reinicioIdTipoProductos()}");
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarTxtCom(){
        txtDescripcionTP.setDisable(false);
    }
    
    public void desactivarTxtCom(){
        txtDescripcionTP.setDisable(true);
    }
    
    public void limpiarControles(){
        txtDescripcionTP.clear();
    }
    
    public Main getEscenarioPrincipalTipoProductos() {
        return escenarioPrincipalTipoProductos;
    }

    public void setEscenarioPrincipalTipoProductos(Main escenarioPrincipalTipoProductos) {
        this.escenarioPrincipalTipoProductos = escenarioPrincipalTipoProductos;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresarTP){
            escenarioPrincipalTipoProductos.menuPrincipalView();
        }
    }
}

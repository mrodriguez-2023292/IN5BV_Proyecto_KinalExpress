
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
import org.mariorodriguez.bean.Compras;
import org.mariorodriguez.db.Conexion;
import org.mariorodriguez.system.Main;

public class ComprasController implements Initializable {
    private Main escenarioPrincipalCompras;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Compras> listarCompras;
    
    /*Botones*/
    @FXML private Button btnRegresarCom;
    @FXML private Button btnAgregarCom;
    @FXML private Button btnEliminarCom;
    @FXML private Button btnEditarCom;
    @FXML private Button btnReportesCom;
    
    /*Imagenes*/
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReportes;
    
    /*Textos*/
    @FXML private TextField txtFechaDoc;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtTotalDoc;
    
    /*Tabla*/
    @FXML private TableView tblCompras;
    
    /*Columnas*/
    @FXML private TableColumn colNumDoc;
    @FXML private TableColumn colFechaDoc;
    @FXML private TableColumn colDes;
    @FXML private TableColumn colTotalDoc;

    public ComprasController() {
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        desactivarTxtCom();
        
        if(listarCompras.isEmpty()){
            reinicioCompras();
        }
    }  
    
    public void cargarDatos(){
        tblCompras.setItems(getCompras());
        colNumDoc.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
        colFechaDoc.setCellValueFactory(new PropertyValueFactory<Compras, String>("fechaDocumento"));
        colDes.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcion"));
        colTotalDoc.setCellValueFactory(new PropertyValueFactory<Compras, Double>("totalDocumento"));
    }
    
    public void selecionarElemento(){
        txtFechaDoc.setText(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getFechaDocumento());
        txtDescripcion.setText(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getDescripcion());        
        txtTotalDoc.setText(String.valueOf(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getTotalDocumento()));
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
    
    public void agregarCompras(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarTxtCom();
                btnAgregarCom.setText("Guardar");
                btnEliminarCom.setText("Cancelar");
                btnEditarCom.setDisable(true);
                btnReportesCom.setDisable(true);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/guardar-el-archivo.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/error.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardarCompras();
                desactivarTxtCom();
                limpiarControles();
                btnAgregarCom.setText("Agregar");
                btnEliminarCom.setText("Eliminar");
                btnEditarCom.setDisable(false);
                btnReportesCom.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-archivo.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/eliminar-documento.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardarCompras(){
        Compras registro = new Compras();
        
        registro.setFechaDocumento(txtFechaDoc.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setTotalDocumento(Double.parseDouble(txtTotalDoc.getText()));

        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCompras(?, ?, ?)}");
            procedimiento.setString(1, registro.getFechaDocumento());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setDouble(3, registro.getTotalDocumento());
            procedimiento.execute();
            
            listarCompras.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarCompras(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                limpiarControles();
                btnAgregarCom.setText("Agregar");
                btnEliminarCom.setText("Eliminar");
                btnEditarCom.setDisable(false);
                btnReportesCom.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-archivo.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/eliminar-documento.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                desactivarTxtCom();
                break;
            default:
                if(tblCompras.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confimar si desea eliminar el registro", "Eliminar Compras", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCompras(?)}");
                            procedimiento.setInt(1, ((Compras)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            
                            listarCompras.remove(tblCompras.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                    if(listarCompras.isEmpty()){
                        reinicioCompras();
                    }
                    limpiarControles();
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento");
                }
                break;
            
        }
    }
    
    public void editarCompra(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblCompras.getSelectionModel().getSelectedItem() !=null){
                    activarTxtCom();
                    btnEditarCom.setText("Actualizar");
                    btnReportesCom.setText("Cancelar");
                    btnAgregarCom.setDisable(true);
                    btnEliminarCom.setDisable(true);
                    imgEditar.setImage(new Image("/org/mariorodriguez/images/editar.png"));
                    imgReportes.setImage(new Image("/org/mariorodriguez/images/error.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar algun elemento");
                }
                break;
            case ACTUALIZAR:
                actualizarCompra();
                btnEditarCom.setText("Editar");
                btnReportesCom.setText("Reporte");
                btnAgregarCom.setDisable(false);
                btnEliminarCom.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/usuario editar.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                desactivarTxtCom();
                break;
        }
    }
    
    public void reportesCompras(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarTxtCom();
                limpiarControles();
                btnEditarCom.setText("Editar");
                btnReportesCom.setText("Reportes");
                btnAgregarCom.setDisable(false);
                btnEliminarCom.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/editar-documento.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizarCompra(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarCompras(?, ?, ?, ?)}");
            Compras registro = (Compras)tblCompras.getSelectionModel().getSelectedItem();
            
            registro.setFechaDocumento(txtFechaDoc.getText());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setTotalDocumento(Double.parseDouble(txtTotalDoc.getText()));
            
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getFechaDocumento());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.getTotalDocumento());
            procedimiento.execute();

            listarCompras.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }        
            
    public void reinicioCompras(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_reinicioIdCompras()}");
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarTxtCom(){
        txtFechaDoc.setDisable(false);
        txtDescripcion.setDisable(false);
        txtTotalDoc.setDisable(false);
    }
    
    public void desactivarTxtCom(){
        txtFechaDoc.setDisable(true);
        txtDescripcion.setDisable(true);
        txtTotalDoc.setDisable(true);
    }
    
    public void limpiarControles(){
        txtFechaDoc.clear();
        txtDescripcion.clear();
        txtTotalDoc.clear();
    }

    public Main getEscenarioPrincipalCompras() {
        return escenarioPrincipalCompras;
    }

    public void setEscenarioPrincipalCompras(Main escenarioPrincipalCompras) {
        this.escenarioPrincipalCompras = escenarioPrincipalCompras;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresarCom){
            escenarioPrincipalCompras.menuPrincipalView();
        }
    }
}

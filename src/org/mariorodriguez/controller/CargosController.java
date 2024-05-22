
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
import org.mariorodriguez.bean.Cargos;
import org.mariorodriguez.db.Conexion;
import org.mariorodriguez.system.Main;

public class CargosController implements Initializable {
    private Main escenarioPrincipalCargos;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Cargos> listarCargos;

    /*Botones*/
    @FXML private Button btnRegresarCE;
    @FXML private Button btnAgregarCE;
    @FXML private Button btnEliminarCE;
    @FXML private Button btnEditarCE;
    @FXML private Button btnReportesCE;
    
    /*Imagenes*/
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReportes;
    
    /*Textos*/
    @FXML private TextField txtNomCargo;
    @FXML private TextField txtDesCargo;
    
    /*Tabla*/
    @FXML private TableView tblCompras;
    
    /*Columnas*/
    @FXML private TableColumn colCodCE;
    @FXML private TableColumn colNomCargo;
    @FXML private TableColumn colDesCargo;
    
    public CargosController() {
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        desactivarTxtCargos();
        
        if(listarCargos.isEmpty()){
            reinicioCargos();
        }
    }      

    public void cargarDatos(){
        tblCompras.setItems(getCargos());
        colCodCE.setCellValueFactory(new PropertyValueFactory<Cargos, Integer>("codigoCargoEmpleado"));
        colNomCargo.setCellValueFactory(new PropertyValueFactory<Cargos, String>("nombreCargo"));
        colDesCargo.setCellValueFactory(new PropertyValueFactory<Cargos, String>("descripcionCargo"));
    }
    
    public void selecionarElemento(){
        txtNomCargo.setText(((Cargos)tblCompras.getSelectionModel().getSelectedItem()).getNombreCargo());
        txtDesCargo.setText(((Cargos)tblCompras.getSelectionModel().getSelectedItem()).getDescripcionCargo());
    }
    
    public ObservableList<Cargos> getCargos(){
        ArrayList<Cargos> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargoEmpleado()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                Lista.add(new Cargos (resultado.getInt("codigoCargoEmpleado"),
                                        resultado.getString("nombreCargo"),
                                        resultado.getString("descripcionCargo")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarCargos = FXCollections.observableArrayList(Lista);
    }
    
    public void agregarCargos(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarTxtCargos();
                btnAgregarCE.setText("Guardar");
                btnEliminarCE.setText("Cancelar");
                btnEditarCE.setDisable(true);
                btnReportesCE.setDisable(true);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/guardar-el-archivo.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/error.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardarCargos();
                desactivarTxtCargos();
                limpiarControles();
                btnAgregarCE.setText("Agregar");
                btnEliminarCE.setText("Eliminar");
                btnEditarCE.setDisable(false);
                btnReportesCE.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-cargo.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/eliminar-cargo.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardarCargos(){
        Cargos registro = new Cargos();
        
        registro.setNombreCargo(txtNomCargo.getText());
        registro.setDescripcionCargo(txtDesCargo.getText());

        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargoEmpleado(?, ?)}");
            procedimiento.setString(1, registro.getNombreCargo());
            procedimiento.setString(2, registro.getDescripcionCargo());
            procedimiento.execute();
            
            listarCargos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarCargos(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                limpiarControles();
                btnAgregarCE.setText("Agregar");
                btnEliminarCE.setText("Eliminar");
                btnEditarCE.setDisable(false);
                btnReportesCE.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-cargo.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/eliminar-cargo.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                desactivarTxtCargos();
                break;
            default:
                if(tblCompras.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confimar si desea eliminar el registro", "Eliminar cargos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCargoEmpleado(?)}");
                            procedimiento.setInt(1, ((Cargos)tblCompras.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                            procedimiento.execute();
                            
                            listarCargos.remove(tblCompras.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                    if(listarCargos.isEmpty()){
                        reinicioCargos();
                    }
                    limpiarControles();
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento");
                }
                break;
            
        }
    }
    
    public void editarCargos(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblCompras.getSelectionModel().getSelectedItem() !=null){
                    activarTxtCargos();
                    btnEditarCE.setText("Actualizar");
                    btnReportesCE.setText("Cancelar");
                    btnAgregarCE.setDisable(true);
                    btnEliminarCE.setDisable(true);
                    imgEditar.setImage(new Image("/org/mariorodriguez/images/editar.png"));
                    imgReportes.setImage(new Image("/org/mariorodriguez/images/error.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar algun elemento");
                }
                break;
            case ACTUALIZAR:
                actualizarCargos();
                btnEditarCE.setText("Editar");
                btnReportesCE.setText("Reporte");
                btnAgregarCE.setDisable(false);
                btnEliminarCE.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/usuario editar.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                desactivarTxtCargos();
                break;
        }
    }
    
    public void reportesCargos(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarTxtCargos();
                limpiarControles();
                btnEditarCE.setText("Editar");
                btnReportesCE.setText("Reportes");
                btnAgregarCE.setDisable(false);
                btnEliminarCE.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/editar-cargo.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizarCargos(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarCargoEmpleado(?, ?, ?)}");
            Cargos registro = (Cargos)tblCompras.getSelectionModel().getSelectedItem();
            
            registro.setNombreCargo(txtNomCargo.getText());
            registro.setDescripcionCargo(txtDesCargo.getText());
            
            procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
            procedimiento.execute();

            listarCargos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }        
            
    public void reinicioCargos(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_reinicioIdCargoEmpleado()}");
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarTxtCargos(){
        txtNomCargo.setDisable(false);
        txtDesCargo.setDisable(false);
    }
    
    public void desactivarTxtCargos(){
        txtNomCargo.setDisable(true);
        txtDesCargo.setDisable(true);
    }
    
    public void limpiarControles(){
        txtNomCargo.clear();
        txtDesCargo.clear();
    }
    
    public Main getEscenarioPrincipalCargos() {
        return escenarioPrincipalCargos;
    }

    public void setEscenarioPrincipalCargos(Main escenarioPrincipalCargos) {
        this.escenarioPrincipalCargos = escenarioPrincipalCargos;
    }

    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresarCE){
            escenarioPrincipalCargos.menuPrincipalView();
        }
    }
}

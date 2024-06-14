
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
import org.mariorodriguez.bean.Empleados;
import org.mariorodriguez.db.Conexion;
import org.mariorodriguez.system.Main;

public class EmpleadosController implements Initializable{
    private Main escenarioPrincipalEmpleados;
    
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Empleados> listarEmpleados;
    private ObservableList<Cargos> listarCargos;
    
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
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtSueldo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTurno;
    
    /*Combo box*/
    @FXML private ComboBox cmbIdCargos;
    
    /*Tabla*/
    @FXML private TableView tblEmpleados;
    
    /*Columnas*/
    @FXML private TableColumn colID;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colSueldo;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colIdCargos;
    
    public EmpleadosController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {  
        cargarDatos();
        desactivarTxt();
        cmbIdCargos.setItems(getCargos());
        
        if(listarEmpleados.isEmpty()){
            reinicioId();
        }
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colID.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleado"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombreEmpleado"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidoEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccionEmpleado"));
        colTurno.setCellValueFactory(new PropertyValueFactory<Empleados, String>("turno"));
        colIdCargos.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("CargoEmpleado_codigoCargoEmpleado"));
    }

    public void selecionarElemento(){
        txtNombres.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado());
        txtApellidos.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado());
        txtSueldo.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));        
        txtDireccion.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
        txtTurno.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getTurno());
        cmbIdCargos.getSelectionModel().select(buscarC(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCargoEmpleado_codigoCargoEmpleado()));
    }
    
    public Cargos buscarC(int codigoCargoEmpleado){
        Cargos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargoEmpleado(?)}");
            procedimiento.setInt(1, codigoCargoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Cargos(registro.getInt("codigoCargoEmpleado"),
                                              registro.getString("nombreCargo"),
                                              registro.getString("descripcionCargo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
    
    public void agregarEmpleado(){
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
        Empleados registro = new Empleados();

        registro.setNombreEmpleado(txtNombres.getText());
        registro.setApellidoEmpleado(txtApellidos.getText());
        registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
        registro.setDireccionEmpleado(txtDireccion.getText());
        registro.setTurno(txtTurno.getText());
        registro.setCargoEmpleado_codigoCargoEmpleado(((Cargos)cmbIdCargos.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
       
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEmpleado(?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getNombreEmpleado());
            procedimiento.setString(2, registro.getApellidoEmpleado());
            procedimiento.setDouble(3, registro.getSueldo());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTurno());
            procedimiento.setInt(6, registro.getCargoEmpleado_codigoCargoEmpleado());
            procedimiento.execute();
            
            listarEmpleados.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarEmpleado(){
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
                if(tblEmpleados.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confimar si desea eliminar el registro", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                            procedimiento.setInt(1, ((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            
                            listarEmpleados.remove(tblEmpleados.getSelectionModel().getSelectedItem());
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
    
    public void editarEmpleado(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarEmpleado(?, ?, ?, ?, ?, ?, ?)}");
            Empleados registro = (Empleados)tblEmpleados.getSelectionModel().getSelectedItem();
            
            registro.setNombreEmpleado(txtNombres.getText());
            registro.setApellidoEmpleado(txtApellidos.getText());
            registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
            registro.setDireccionEmpleado(txtDireccion.getText());
            registro.setTurno(txtTurno.getText());
            registro.setCargoEmpleado_codigoCargoEmpleado(((Cargos)cmbIdCargos.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());

            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setString(2, registro.getNombreEmpleado());
            procedimiento.setString(3, registro.getApellidoEmpleado());
            procedimiento.setDouble(4, registro.getSueldo());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTurno());
            procedimiento.setInt(7, registro.getCargoEmpleado_codigoCargoEmpleado());
            procedimiento.execute();

            listarEmpleados.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reinicioId(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_reinicioIdEmpleados()}");
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarTxt(){
        txtNombres.setDisable(false);
        txtApellidos.setDisable(false);
        txtSueldo.setDisable(false);
        txtDireccion.setDisable(false);
        txtTurno.setDisable(false);
        cmbIdCargos.setDisable(false);
    }
    
    public void desactivarTxt(){
        txtNombres.setDisable(true);
        txtApellidos.setDisable(true);
        txtSueldo.setDisable(true);
        txtDireccion.setDisable(true);
        txtTurno.setDisable(true);
        cmbIdCargos.setDisable(true);
    }
    
    public void limpiarControles(){
        txtNombres.clear();
        txtApellidos.clear();
        txtSueldo.clear();
        txtDireccion.clear();
        txtTurno.clear();
        cmbIdCargos.getSelectionModel().getSelectedItem();
    }
    
    public Main getEscenarioPrincipalEmpleados() {
        return escenarioPrincipalEmpleados;
    }

    public void setEscenarioPrincipalEmpleados(Main escenarioPrincipalEmpleados) {
        this.escenarioPrincipalEmpleados = escenarioPrincipalEmpleados;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipalEmpleados.menuPrincipalView();
        }
    } 
}

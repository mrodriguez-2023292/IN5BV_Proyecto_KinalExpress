
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
import javafx.scene.image.ImageView;
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
    @FXML private Button btnRegresarEm;
    @FXML private Button btnAgregarEm;
    @FXML private Button btnEliminarEm;
    @FXML private Button btnEditarEm;
    @FXML private Button btnReportesEm;
    
    /*Imagenes*/
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReportes;
    
    /*Textos*/
    @FXML private TextField txtNombresEm;
    @FXML private TextField txtApellidosEm;
    @FXML private TextField txtDireccionEm;
    @FXML private TextField txtSueldoEm;
    
    /*Combo box*/
    @FXML private ComboBox cmdTurnoEm;
    @FXML private ComboBox cmbIdCargoEm;
    
    /*Tabla*/
    @FXML private TableView tblEmpleados;
    
    /*Columnas*/
    @FXML private TableColumn colCodEm;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colSueldo;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colIdCargoEm;
    
    public EmpleadosController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colCodEm.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleado"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombreEmpleado"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidoEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccionEmpleado"));
        colTurno.setCellValueFactory(new PropertyValueFactory<Empleados, String>("turno"));
        colIdCargoEm.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("CargoEmpleado_codigoCargoEmpleado"));
    }

    public void selecionarElemento(){
        txtNombresEm.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado());
        txtApellidosEm.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado());
        txtSueldoEm.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));        
        txtDireccionEm.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
        cmdTurnoEm.getSelectionModel().select(buscarT(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem())));
        cmbIdCargoEm.getSelectionModel().select(buscarC(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCargoEmpleado_codigoCargoEmpleado()));
    }
    
    public Empleados buscarT(int codigoEmpleado){
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
    
    public Main getEscenarioPrincipalEmpleados() {
        return escenarioPrincipalEmpleados;
    }

    public void setEscenarioPrincipalEmpleados(Main escenarioPrincipalEmpleados) {
        this.escenarioPrincipalEmpleados = escenarioPrincipalEmpleados;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresarEm){
            escenarioPrincipalEmpleados.menuPrincipalView();
        }
    } 
}

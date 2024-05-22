
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
import org.mariorodriguez.bean.Clientes;
import org.mariorodriguez.db.Conexion;
import org.mariorodriguez.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuClientesController implements Initializable {
    private Main escenarioPrincipalClientes;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Clientes> listarClientes;
    
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
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtNIT;
    
    /*Tabla*/
    @FXML private TableView tblClientes;
    
    /*Columnas*/
    @FXML private TableColumn colCodigoC;
    @FXML private TableColumn colNITC;
    @FXML private TableColumn colNombreC;
    @FXML private TableColumn colApellidoC;
    @FXML private TableColumn colDireccionC;
    @FXML private TableColumn colTelefonoC;
    @FXML private TableColumn colCorreoC;

    public MenuClientesController() {
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        txtNombre.setDisable(true);
        txtApellido.setDisable(true);
        txtTelefono.setDisable(true);
        txtCorreo.setDisable(true);
        txtDireccion.setDisable(true);
        txtNIT.setDisable(true);
        
        if(listarClientes.isEmpty()){
            reinicio();
        }
    }    

    public void cargarDatos(){
        tblClientes.setItems(getClientes());
        colCodigoC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoCliente"));
        colNITC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("NITcliente"));
        colNombreC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombreCliente"));
        colApellidoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidoCliente"));
        colDireccionC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionCliente"));
        colTelefonoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoCliente"));
        colCorreoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("correoCliente"));
    }
    
    public void selecionarElemento(){
        txtNIT.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNITcliente());
        txtNombre.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNombreCliente());
        txtApellido.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getApellidoCliente());
        txtDireccion.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getDireccionCliente());
        txtTelefono.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getTelefonoCliente());
        txtCorreo.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCorreoCliente());
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

    public void agregar(){
        txtNombre.setDisable(false);
        txtApellido.setDisable(false);
        txtTelefono.setDisable(false);
        txtCorreo.setDisable(false);
        txtDireccion.setDisable(false);
        txtNIT.setDisable(false);
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReportes.setDisable(true);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/guardar-el-archivo.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/error.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-usuario.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/quitar-usuario.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                activarControles();
                cargarDatos();
                txtNombre.setDisable(true);
                txtApellido.setDisable(true);
                txtTelefono.setDisable(true);
                txtCorreo.setDisable(true);
                txtDireccion.setDisable(true);
                txtNIT.setDisable(true);
                break;
        }
    }
    
    public void guardar(){
        Clientes registro = new Clientes();
        
        registro.setNITcliente(txtNIT.getText());
        registro.setNombreCliente(txtNombre.getText());
        registro.setApellidoCliente(txtApellido.getText());
        registro.setDireccionCliente(txtDireccion.getText());
        registro.setTelefonoCliente(txtTelefono.getText());
        registro.setCorreoCliente(txtCorreo.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarClientes(?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getNITcliente());
            procedimiento.setString(2, registro.getNombreCliente());
            procedimiento.setString(3, registro.getApellidoCliente());
            procedimiento.setString(4, registro.getDireccionCliente());
            procedimiento.setString(5, registro.getTelefonoCliente());
            procedimiento.setString(6, registro.getCorreoCliente());
            procedimiento.execute();
            
            listarClientes.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/mariorodriguez/images/agregar-usuario.png"));
                imgEliminar.setImage(new Image("/org/mariorodriguez/images/quitar-usuario.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                txtNombre.setDisable(true);
                txtApellido.setDisable(true);
                txtTelefono.setDisable(true);
                txtCorreo.setDisable(true);
                txtDireccion.setDisable(true);
                txtNIT.setDisable(true);
                break;
            default:
                if(tblClientes.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confimar si desea eliminar el registro", "Eliminar Clientes", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarClientes(?)}");
                            procedimiento.setInt(1, ((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            
                            listarClientes.remove(tblClientes.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                    if(listarClientes.isEmpty()){
                        reinicio();
                    }
                    limpiarControles();
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento");
                }
                break;
            
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblClientes.getSelectionModel().getSelectedItem() !=null){
                    txtNombre.setDisable(false);
                    txtApellido.setDisable(false);
                    txtTelefono.setDisable(false);
                    txtCorreo.setDisable(false);
                    txtDireccion.setDisable(false);
                    txtNIT.setDisable(false);
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/mariorodriguez/images/editar.png"));
                    imgReportes.setImage(new Image("/org/mariorodriguez/images/error.png"));
                    activarControles();
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar algun elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReportes.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/mariorodriguez/images/usuario editar.png"));
                imgReportes.setImage(new Image("/org/mariorodriguez/images/reporte.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                txtNombre.setDisable(true);
                txtApellido.setDisable(true);
                txtTelefono.setDisable(true);
                txtCorreo.setDisable(true);
                txtDireccion.setDisable(true);
                txtNIT.setDisable(true);
                break;
        }
    }
    
    public void reportes(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                txtNombre.setDisable(true);
                txtApellido.setDisable(true);
                txtTelefono.setDisable(true);
                txtCorreo.setDisable(true);
                txtDireccion.setDisable(true);
                txtNIT.setDisable(true);
                desactivarControles();
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
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarClientes(?, ?, ?, ?, ?, ?, ?)}");
            Clientes registro = (Clientes)tblClientes.getSelectionModel().getSelectedItem();
            
            registro.setNITcliente(txtNIT.getText());            
            registro.setNombreCliente(txtNombre.getText());
            registro.setApellidoCliente(txtApellido.getText());
            registro.setDireccionCliente(txtDireccion.getText());
            registro.setTelefonoCliente(txtTelefono.getText());
            registro.setCorreoCliente(txtCorreo.getText());
            
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getNITcliente());
            procedimiento.setString(3, registro.getNombreCliente());
            procedimiento.setString(4, registro.getApellidoCliente());
            procedimiento.setString(5, registro.getDireccionCliente());
            procedimiento.setString(6, registro.getTelefonoCliente());
            procedimiento.setString(7, registro.getCorreoCliente());
            procedimiento.execute();

            listarClientes.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reinicio(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_reinicioIdClientes()}");
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtTelefono.setEditable(false);
        txtCorreo.setEditable(false);
        txtDireccion.setEditable(false);
        txtNIT.setEditable(false);
    }
    
    public void activarControles(){
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtTelefono.setEditable(true);
        txtCorreo.setEditable(true);
        txtDireccion.setEditable(true);
        txtNIT.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtDireccion.clear();
        txtNIT.clear();
    }
    
    public Main getEscenarioPrincipalClientes() {
        return escenarioPrincipalClientes;
    }
    
    public void setEscenarioPrincipalClientes(Main escenarioPrincipalClientes) {
        this.escenarioPrincipalClientes = escenarioPrincipalClientes;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipalClientes.menuPrincipalView();
        }
    }
}
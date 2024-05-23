
package org.mariorodriguez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import org.mariorodriguez.system.Main;

/*Herencia Multiple concepto, interfaces. POO.
 */
public class MenuPrincipalController implements Initializable{
    private Main escenarioPrincipal;
    
    @FXML private MenuItem btnMenuClientes;
    @FXML private MenuItem btnProveedores;
    @FXML private MenuItem btnProgramador;
    @FXML private MenuItem btnCompras;
    @FXML private MenuItem btnProductos;
    @FXML private MenuItem btnTipoProducto;
    @FXML private MenuItem btnCargos;
    @FXML private Button btnSalir;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @FXML
    public void click(ActionEvent evento){
        if(evento.getSource() == btnMenuClientes){
            escenarioPrincipal.menuClienteView();
        }else if(evento.getSource() == btnProgramador){
            escenarioPrincipal.programadorView();
        }else if(evento.getSource() == btnProveedores){
            escenarioPrincipal.proveedoresView();
        }else if(evento.getSource() == btnCompras){
            escenarioPrincipal.comprasView();
        }else if(evento.getSource() == btnProductos){
            escenarioPrincipal.productosView();
        }else if(evento.getSource() == btnTipoProducto){
            escenarioPrincipal.tipoProductoView();
        }else if(evento.getSource() == btnCargos){
            escenarioPrincipal.cargosView();
        }else if(evento.getSource() == btnSalir){
            System.exit(0);
        }
    }
}

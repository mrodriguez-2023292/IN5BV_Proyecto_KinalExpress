
package org.mariorodriguez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.mariorodriguez.system.Main;

/*Herencia Multiple concepto, interfaces. POO.
 */
public class ProgramadorController implements Initializable{
    private Main escenarioProgramador;
    
    @FXML private Button btnRegresar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Main getEscenarioProgramador() {
        return escenarioProgramador;
    }

    public void setEscenarioProgramador(Main escenarioProgramador) {
        this.escenarioProgramador = escenarioProgramador;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioProgramador.menuPrincipalView();
        }
    }
}

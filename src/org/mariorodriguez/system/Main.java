
package org.mariorodriguez.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mariorodriguez.controller.CargosController;
import org.mariorodriguez.controller.ComprasController;
import org.mariorodriguez.controller.MenuClientesController;
import org.mariorodriguez.controller.MenuPrincipalController;
import org.mariorodriguez.controller.ProductosController;
import org.mariorodriguez.controller.ProgramadorController;
import org.mariorodriguez.controller.ProveedorController;
import org.mariorodriguez.controller.TipoProductosController;

/**
 *Documentacion
  -Nombre Mario Andreé Rodríguez Zamboni
  -Fecha de creacion: 11/04
  -Fecha de modificacion: 10/05
  -@autor mrodriguez-2023292
 **/

public class Main extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String URLVIEW = "/org/mariorodriguez/view/";
            
    /* FXMLLoader, Parent, / que se usa para separar*/
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("KinalStore_RapiMarket");
        escenarioPrincipal.getIcons().add(new Image(Main.class.getResourceAsStream("/org/mariorodriguez/images/Logo2.png")));
        menuPrincipalView();
        //Parent root = FXMLLoader.load(getClass().getResource("/org/mariorodriguez/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        escenarioPrincipal.show();
    }

    public Initializable cambiarEscena(String fxmlName, int width, int heigth) throws Exception{
        Initializable resultado;
        FXMLLoader loader = new FXMLLoader();
        
        InputStream file = Main.class.getResourceAsStream(URLVIEW + fxmlName);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(URLVIEW + fxmlName));
        
        escena = new Scene((AnchorPane)loader.load(file), width, heigth);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        
        resultado = (Initializable)loader.getController();
        
        return resultado;
    }
    
    public void menuPrincipalView (){
        try{
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 613, 613);
            menuPrincipalView.setEscenarioPrincipal(this);  
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuClienteView(){
        try{
            MenuClientesController menuClienteView = (MenuClientesController)cambiarEscena("MenuClientesView.fxml", 1017, 572);
            menuClienteView.setEscenarioPrincipalClientes(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void proveedoresView(){
        try{
            ProveedorController proveedoresView = (ProveedorController)cambiarEscena("ProveedoresView.fxml", 1017, 572);
            proveedoresView.setEscenarioPrincipalProveedores(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void programadorView(){
        try{
            ProgramadorController programadorView = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 610, 528);
            programadorView.setEscenarioProgramador(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void comprasView(){
        try{
            ComprasController comprasView = (ComprasController)cambiarEscena("ComprasView.fxml", 1024, 575);
            comprasView.setEscenarioPrincipalCompras(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void productosView(){
        try{
            ProductosController productosView = (ProductosController)cambiarEscena("ProductosView.fxml", 1017, 562);
            productosView.setEscenarioPrincipalProductos(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void tipoProductoView(){
        try{
            TipoProductosController tipoProductoView = (TipoProductosController)cambiarEscena("TipoProductoView.fxml", 1024, 575);
            tipoProductoView.setEscenarioPrincipalTipoProductos(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargosView(){
        try{
            CargosController cargosView = (CargosController)cambiarEscena("CargoEmpleadosView.fxml", 1024, 575);
            cargosView.setEscenarioPrincipalCargos(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}

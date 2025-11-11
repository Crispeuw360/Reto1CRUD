/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.User_;

/**
 * Controlador para la ventana de baja de usuarios (Drop Out).
 * Permite a los usuarios eliminar permanentemente su cuenta del sistema
 * después de confirmar su identidad. Incluye medidas de seguridad para
 * prevenir eliminaciones accidentales.
 * 
 * @author pikain
 * @version 1.0
 */
public class DropOutController implements Initializable {

    // Componentes de la interfaz de usuario
    @FXML
    private Button ConfirmBtn;
    @FXML
    private Label label;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Circle avatarIcon;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label passwordConfirmLabel;
    @FXML
    private PasswordField passwordConfirmField;
    @FXML
    private Label deleteLabel;
    @FXML
    private Button exitBtn;
    
    // Controlador para la lógica de negocio
    private Controller con = new Controller();

    /**
     * Initializes the controller class.
     * Configura el estado inicial de los componentes de la interfaz.
     * 
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO - Puede ser utilizado para inicializaciones adicionales
    }

    /**
     * Establece el nombre de usuario en el campo correspondiente.
     * Utilizado para pre-cargar el username del usuario que desea darse de baja.
     * 
     * @param username El nombre de usuario a mostrar en el campo
     */
    public void setUsername(String username) {
        this.usernameField.setText(username);
    }

    /**
     * Maneja el evento de eliminación de cuenta.
     * Elimina permanentemente el usuario del sistema después de obtener
     * sus datos desde la base de datos. Si la eliminación es exitosa,
     * redirige a la ventana de login.
     * 
     * @param event El evento de acción que desencadenó este método
     */
    @FXML
    private void onDelete(ActionEvent event) {
        // Obtener el usuario desde la base de datos
        User_ user = con.getUserByUsername(usernameField.getText());
        
        // Intentar eliminar el usuario
        boolean ok = con.deleteUser(user.getUser_name(), user.getUser_code());
        
        if (ok) {
            showAlert("Usuario eliminado correctamente.", Alert.AlertType.INFORMATION);
            exitBtn.setDisable(false);
            
            try {
                // Cargar el FXML de la ventana de login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"));
                Parent root = loader.load();

                // Crear la escena y ventana (Stage)
                Stage stage = new Stage();
                stage.setTitle("Iniciar Sesión");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();

                // Cerrar la ventana actual
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error al volver");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo abrir la ventana de login.");
                alert.showAndWait();
            }
        } else {
            showAlert("Error al eliminar el usuario.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Maneja el evento del botón "Volver".
     * Cancela el proceso de baja y redirige a la ventana de modificación de perfil
     * manteniendo los datos del usuario actual.
     * 
     * @param event El evento de acción que desencadenó este método
     */
    @FXML
    private void OnBack(ActionEvent event) {
        try {
            // Cargar el FXML de la ventana de modificación
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyWindow.fxml"));
            Parent root = loader.load();

            // Configurar el controlador con los datos del usuario actual
            ModifyWindowController modifyController = loader.getController();
            User_ user = con.getUserByUsername(usernameField.getText());
            modifyController.setUser(user);

            // Crear la escena y ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Iniciar Sesión");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al volver");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo abrir la ventana de login.");
            alert.showAndWait();
        }
    }

    /**
     * Muestra una alerta al usuario con el mensaje y tipo especificados.
     * 
     * @param msg El mensaje a mostrar en la alerta
     * @param type El tipo de alerta (ERROR, INFORMATION, WARNING, etc.)
     */
    private void showAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
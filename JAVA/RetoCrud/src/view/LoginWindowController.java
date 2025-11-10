/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User_;

/**
 * Controlador para la ventana de inicio de sesión.
 * Maneja la autenticación de usuarios, redireccionando a diferentes vistas
 * según el tipo de usuario (administrador o usuario normal).
 * 
 * @author pikain
 * @version 1.0
 */
public class LoginWindowController implements Initializable {

    // Componentes de la interfaz de usuario
    @FXML
    private AnchorPane backgroundPanel;
    @FXML
    private Button loginBtn;
    @FXML
    private Label label;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private Button signupBtn;
    @FXML
    private Label msgLabel;

    // Controlador para la lógica de negocio
    private Controller con = new Controller();
    
    /**
     * Initializes the controller class.
     * Configura los listeners para habilitar/deshabilitar el botón de login
     * según si los campos están completos.
     * 
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Deshabilitar el botón hasta que ambos campos estén rellenos
        loginBtn.setDisable(true);

        usernameField.textProperty().addListener((obs, oldV, newV) -> checkFields());
        passwordField.textProperty().addListener((obs, oldV, newV) -> checkFields());
    }

    /**
     * Verifica si ambos campos (usuario y contraseña) están completos
     * y habilita o deshabilita el botón de login en consecuencia.
     */
    private void checkFields() {
        boolean filled = !usernameField.getText().isEmpty() && !passwordField.getText().isEmpty();
        loginBtn.setDisable(!filled);
    } 

    /**
     * Maneja el evento de inicio de sesión.
     * Valida las credenciales del usuario y redirige a la vista correspondiente
     * según su tipo (administrador o usuario normal).
     */
    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        boolean existe = con.existUser(username);
        if (!existe){
            showError("No existe el usuario");
                    
        }
        else{
            String password = passwordField.getText();
            boolean valido = con.validatePassword(username, password);
            if (valido){
                showSuccess("USUARIO ENCONTRADO");
                System.out.println("encontrado");
                boolean isAdmin = con.isAdmin(username);
                
                
                try {
                    FXMLLoader loader;
                    Parent root;
                    Stage stage = new Stage();

                    if (isAdmin) {
                        // 🔸 Si es admin, cargar AdminView.fxml
                        loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
                        root = loader.load();

                       
                        stage.setTitle("Panel de Administración");
                    } else {
                        // 🔹 Si es usuario normal, cargar ModifyWindow.fxml
                        loader = new FXMLLoader(getClass().getResource("/view/ModifyWindow.fxml"));
                        root = loader.load();

                        ModifyWindowController modifyController = loader.getController();
                        User_ user = con.getUserByUsername(usernameField.getText());
                        modifyController.setUser(user);

                        stage.setTitle("Modificar perfil");
                    }

                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();

                    // Cerrar la ventana de login
                    Stage currentStage = (Stage) signupBtn.getScene().getWindow();
                    currentStage.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    showError("No se pudo abrir la ventana correspondiente.");
                }
                clearFields();
            }
            else{
                showSuccess("USUARIO NO ENCONTRADO");
                clearFields();
            }
        }
    }

    /**
     * Maneja el evento de registro de nuevo usuario.
     * Abre la ventana de registro y cierra la ventana actual de login.
     */
    @FXML
    private void onSignUp() {
        try {
            // Cargar el FXML de la ventana de registro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUp.fxml"));
            Parent root = loader.load();

            // Crear nueva escena y ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Registro de usuario");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la actual (opcional)
            Stage currentStage = (Stage) signupBtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showError("No se pudo abrir la ventana de registro.");
        }
    }

    /**
     * Muestra un mensaje de error al usuario.
     * 
     * @param msg El mensaje de error a mostrar
     */
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de éxito o información al usuario.
     * 
     * @param msg El mensaje de éxito a mostrar
     */
    private void showSuccess(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login correcto");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Limpia los campos de usuario y contraseña.
     */
    private void clearFields(){
        usernameField.setText("");
        passwordField.setText("");
    }
    
}
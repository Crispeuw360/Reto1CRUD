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

import javafx.application.Platform;
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
 * FXML Controller class
 *
 * @author pablo
 */
public class LoginWindowController implements Initializable {

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

    private Controller con = new Controller();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Deshabilitar el botón hasta que ambos campos estén rellenos
        loginBtn.setDisable(true);

        usernameField.textProperty().addListener((obs, oldV, newV) -> checkFields());
        passwordField.textProperty().addListener((obs, oldV, newV) -> checkFields());
    }

    private void checkFields() {
        boolean filled = !usernameField.getText().isEmpty() && !passwordField.getText().isEmpty();
        loginBtn.setDisable(!filled);
    }

    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        setButtonsDisabled(true);

        // Pasar 'this' como referencia al controlador
        con.loginAsync(username, password, (Stage) loginBtn.getScene().getWindow(), this);

        System.out.println("Proceso de login asíncrono iniciado...");
    }

    /**
     * Habilita o deshabilita los botones durante el proceso de login
     */
    private void setButtonsDisabled(boolean disabled) {
        loginBtn.setDisable(disabled);
        signupBtn.setDisable(disabled);
        loginBtn.setText(disabled ? "Iniciando..." : "Iniciar Sesión");
    }

    /**
     * Método público para re-habilitar los botones desde fuera
     */
    public void enableButtons() {
        setButtonsDisabled(false);
        clearFields();
    }

    @FXML
    private void onSignUp() {
        try {
            // Cargar el FXML de la ventana de registro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
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

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showSuccess(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login correcto");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

}

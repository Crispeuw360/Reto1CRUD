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
 * FXML Controller class
 *
 * @author 2dami
 */
public class DropOutController implements Initializable {

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
    private Controller con = new Controller();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setUsername(String username) {
        this.usernameField.setText(username);
    }

    @FXML
    private void onDelete(ActionEvent event) {
        User_ user = con.getUserByUsername(usernameField.getText());
        boolean ok = con.deleteUser(user.getUser_name(),user.getUser_code());
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

    @FXML
    private void OnBack(ActionEvent event) {
        try {
            // Cargar el FXML de la ventana de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyWindow.fxml"));
            Parent root = loader.load();

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

    private void showAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}

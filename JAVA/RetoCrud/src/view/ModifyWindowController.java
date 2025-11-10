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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User_;

/**
 * FXML Controller class
 *
 * @author pablo
 */
public class ModifyWindowController implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private Button btnModify;
    @FXML
    private TextField fieldUser;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldSurname;
    @FXML
    private TextField fieldGmail;
    @FXML
    private TextField fieldTel;
    @FXML
    private PasswordField fieldPass;
    @FXML
    private PasswordField fieldPass2;
    @FXML
    private Label userLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label passLabel1;
    @FXML
    private Label passLabel2;
    @FXML
    private Label mailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Button btnShow;
    @FXML
    private TextField fieldCard;
    @FXML
    private Label cardLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private ComboBox<String> comboGender;
    @FXML
    private Button btnSave;

    private Controller con = new Controller();
    private boolean passwordVisible = false;
    private User_ currentUser;
    @FXML
    private Button btnDelete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        if (comboGender != null) {
            comboGender.getItems().clear(); // por si acaso
            comboGender.getItems().addAll("Masculino", "Femenino", "Otro");
            comboGender.setDisable(true); // bloqueado al inicio
        }
    }

    public void setUser(User_ user) {
        this.currentUser = user;

        // Mostrar los datos en los TextFields
        fieldName.setText(user.getName_());
        fieldGmail.setText(user.getEmail());
        fieldSurname.setText(user.getSurname());
        fieldUser.setText(user.getUser_name());
        fieldTel.setText(String.valueOf(user.getTelephone()));
        fieldCard.setText(String.valueOf(user.getCard_no()));
        fieldPass.setText(user.getPasswd());
        fieldPass2.setText(user.getPasswd());
        fieldUser.setText(user.getUser_name());
        comboGender.setValue(user.getGender());
        btnSave.setDisable(true);
        fieldUser.setEditable(false);
        setEditableFields(false);

    }

    @FXML
    private void onBack(ActionEvent event) {
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
    }

    @FXML
    private void onModify(ActionEvent event) {
        setEditableFields(true);
        btnSave.setDisable(false);
        btnModify.setDisable(true);
        userLabel.setDisable(true);
    }

    @FXML
    private void onRegister(ActionEvent event) {
        // Comprobamos que todos los campos estén completos
        if (fieldUser.getText().isEmpty() ||
                fieldName.getText().isEmpty() ||
                fieldSurname.getText().isEmpty() ||
                fieldGmail.getText().isEmpty() ||
                fieldTel.getText().isEmpty() ||
                fieldPass.getText().isEmpty() ||
                fieldPass2.getText().isEmpty() ||
                fieldCard.getText().isEmpty() ||
                comboGender.getValue() == null) {

            showAlert("Por favor, rellena todos los campos.", Alert.AlertType.ERROR);
            return;
        }

        // Comprobamos que las contraseñas coinciden
        if (!fieldPass.getText().equals(fieldPass2.getText())) {
            showAlert("Las contraseñas no coinciden.", Alert.AlertType.ERROR);
            return;
        }

        // Comprobamos formato del correo
        if (!fieldGmail.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Correo electrónico no válido.", Alert.AlertType.ERROR);
            return;
        }

        // Comprobamos que teléfono y tarjeta son números
        int tel, card;
        try {
            tel = Integer.parseInt(fieldTel.getText());
            card = Integer.parseInt(fieldCard.getText());
        } catch (NumberFormatException e) {
            showAlert("Teléfono o número de tarjeta inválidos (deben ser numéricos).", Alert.AlertType.ERROR);
            return;
        }

        // Si todo es correcto → actualizamos el objeto y llamamos a BD
        currentUser.setUser_name(fieldUser.getText());
        currentUser.setName_(fieldName.getText());
        currentUser.setSurname(fieldSurname.getText());
        currentUser.setEmail(fieldGmail.getText());
        currentUser.setTelephone(tel);
        currentUser.setCard_no(card);
        currentUser.setGender(comboGender.getValue());
        currentUser.setPasswd(fieldPass.getText());

        boolean ok = con.updateUser(currentUser);

        if (ok) {
            showAlert("Usuario actualizado correctamente.", Alert.AlertType.INFORMATION);
            btnSave.setDisable(true);
            btnModify.setDisable(false);
            setEditableFields(false);
        } else {
            showAlert("Error al actualizar el usuario.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void setEditableFields(boolean editable) {

        fieldName.setEditable(editable);
        fieldGmail.setEditable(editable);
        fieldSurname.setEditable(editable);
        fieldTel.setEditable(editable);
        fieldCard.setEditable(editable);
        comboGender.setDisable(!editable); // 👈 este es el truco
        fieldPass.setEditable(editable);
        fieldPass2.setEditable(editable);
    }

    @FXML
    private void showPass(ActionEvent event) {
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            // Mostrar contraseñas en texto plano
            fieldPass.setPromptText(fieldPass.getText());
            fieldPass2.setPromptText(fieldPass2.getText());
            fieldPass.setText("");
            fieldPass2.setText("");
            btnShow.setText("👁"); // Cambiar ícono del botón
        } else {
            // Ocultar contraseñas (volver a modo puntos)
            fieldPass.setText(fieldPass.getPromptText());
            fieldPass2.setText(fieldPass2.getPromptText());
            fieldPass.setPromptText("");
            fieldPass2.setPromptText("");
            btnShow.setText("👁"); // Mantener mismo ícono o cambiar si prefieres
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        try {
            // Cargar el FXML de la ventana de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DropOutWindow.fxml"));
            Parent root = loader.load();
            String username = fieldUser.getText();
            DropOutController controller = loader.getController();
            controller.setUsername(username);

            // Crear la escena y ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Eliminar usuario");
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

}

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

import Exception.ErrorException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
 * Controlador para la ventana de registro de nuevos usuarios.
 * Maneja el proceso de creación de nuevas cuentas de usuario con validación
 * de datos y verificación de duplicados en el sistema.
 * 
 * @author pikain
 * @version 1.0
 */
public class SignUpController implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private Button btnRegistro;
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

    private Controller con = new Controller();

    /**
     * Initializes the controller class.
     * Configura las opciones del ComboBox de género y establece los listeners
     * para validar los campos del formulario en tiempo real.
     * 
     * @param url The location used to resolve relative paths for the root object,
     *            or null if the location is not known.
     * @param rb  The resources used to localize the root object, or null if the
     *            root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboGender.getItems().addAll("Male", "Female", "Other");
        btnRegistro.setDisable(true);

        // Cada vez que el usuario escribe, comprobamos si todos los campos están llenos
        fieldUser.textProperty().addListener((obs, oldVal, newVal) -> checkFields());
        fieldName.textProperty().addListener((obs, oldVal, newVal) -> checkFields());
        fieldSurname.textProperty().addListener((obs, oldVal, newVal) -> checkFields());
        fieldGmail.textProperty().addListener((obs, oldVal, newVal) -> checkFields());
        fieldTel.textProperty().addListener((obs, oldVal, newVal) -> checkFields());
        fieldPass.textProperty().addListener((obs, oldVal, newVal) -> checkFields());
        fieldPass2.textProperty().addListener((obs, oldVal, newVal) -> checkFields());
        fieldCard.textProperty().addListener((obs, oldVal, newVal) -> checkFields());
        comboGender.valueProperty().addListener((obs, oldVal, newVal) -> checkFields());
    }

    /**
     * Verifica si todos los campos obligatorios del formulario están completos
     * y habilita o deshabilita el botón de registro en consecuencia.
     */
    private void checkFields() {
        boolean camposCompletos = true;

        if (fieldUser.getText().isEmpty())
            camposCompletos = false;
        if (fieldName.getText().isEmpty())
            camposCompletos = false;
        if (fieldSurname.getText().isEmpty())
            camposCompletos = false;
        if (fieldGmail.getText().isEmpty())
            camposCompletos = false;
        if (fieldTel.getText().isEmpty())
            camposCompletos = false;
        if (fieldPass.getText().isEmpty())
            camposCompletos = false;
        if (fieldPass2.getText().isEmpty())
            camposCompletos = false;
        if (fieldCard.getText().isEmpty())
            camposCompletos = false;
        if (comboGender.getValue() == null)
            camposCompletos = false;

        btnRegistro.setDisable(!camposCompletos);

    }

    /**
     * Maneja el evento de registro de nuevo usuario.
     * Valida los datos ingresados, verifica que el usuario no exista previamente
     * y crea el nuevo usuario en el sistema si todas las validaciones son exitosas.
     * 
     * @throws ErrorException Si ocurre un error durante el registro.
     */
    @FXML
    public void onRegister() {
        int tel = 0;
        int card = 0;
        try {
            if (fieldPass.getText().equals(fieldPass2.getText())) {

                if (con.getUserByUsername(fieldUser.getText()) != null) {
                    throw new ErrorException("Nombre usuario ya existente.", "Nombre usuario ya existente.");
                }

                if (!fieldGmail.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    throw new ErrorException("Gmail invalido", "Gmail invalido");
                } else {
                    try {
                        tel = Integer.parseInt(fieldTel.getText());
                        card = Integer.parseInt(fieldCard.getText());
                    } catch (NumberFormatException e) {
                        throw new ErrorException("Telefono o numero de tarjeta no numericos",
                                "Telefono o numero de tarjeta no numericos");

                    }
                }
            } else {
                throw new ErrorException("Contraseñas no coinciden", "Contraseñas no coinciden");
            }

            User_ nuevoUser = new User_(
                    0,
                    fieldUser.getText(),
                    fieldPass.getText(),
                    fieldGmail.getText(),
                    fieldName.getText(),
                    fieldSurname.getText(),
                    tel,
                    card,
                    comboGender.getValue());
            if (con.existUser(fieldUser.getText())) {
                throw new ErrorException("Usuario ya existente", "Usuario ya existente");
            } else {
                boolean creado = con.insertUser(nuevoUser);

                if (creado) {
                    showSuccess("Usuario creado correctamente");
                    clearFields();
                }
            }
        } catch (ErrorException e) {

        }
    }

    /**
     * Maneja el evento de volver a la ventana de login.
     * Cierra la ventana actual de registro y abre la ventana de login
     */
    @FXML
    public void onBack() {
        System.out.println("Back");
        try {
            // Cargar el FXML de la ventana de registro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginWindow.fxml"));
            Parent root = loader.load();

            // Crear nueva escena y ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("LogIn");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la actual (opcional)
            Stage currentStage = (Stage) btnBack.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
        }
    }

    /**
     * Muestra un mensaje de éxito al usuario.
     * 
     * @param mensaje El mensaje de éxito a mostrar
     */
    private void showSuccess(String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Limpia todos los campos del formulario de registro.
     */
    private void clearFields() {
        fieldUser.setText("");
        fieldName.setText("");
        fieldSurname.setText("");
        fieldGmail.setText("");
        fieldTel.setText("");
        fieldPass.setText("");
        fieldPass2.setText("");
        fieldCard.setText("");
    }

}

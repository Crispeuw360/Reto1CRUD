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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ImplementsBD;
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

    // Componentes de la interfaz de usuario
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

    // Controladores y estado de la aplicación
    private ImplementsBD bd = new ImplementsBD(); // acceso a BD
    private Controller con = new Controller(); 
    private boolean passwordsVisible = false;    // para mostrar/ocultar contraseñas
    
    /**
     * Initializes the controller class.
     * Configura las opciones del ComboBox de género y establece los listeners
     * para validar los campos del formulario en tiempo real.
     * 
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar opciones de género
        comboGender.getItems().addAll("Male", "Female", "Other");
        btnRegistro.setDisable(true);

        // Establecer listeners para validar campos en tiempo real
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

    if (fieldUser.getText().isEmpty()) camposCompletos = false;
    if (fieldName.getText().isEmpty()) camposCompletos = false;
    if (fieldSurname.getText().isEmpty()) camposCompletos = false;
    if (fieldGmail.getText().isEmpty()) camposCompletos = false;
    if (fieldTel.getText().isEmpty()) camposCompletos = false;
    if (fieldPass.getText().isEmpty()) camposCompletos = false;
    if (fieldPass2.getText().isEmpty()) camposCompletos = false;
    if (fieldCard.getText().isEmpty()) camposCompletos = false;
    if (comboGender.getValue() == null) camposCompletos = false;

    btnRegistro.setDisable(!camposCompletos);
   }
   
   /**
    * Maneja el evento de registro de nuevo usuario.
    * Valida los datos ingresados, verifica que el usuario no exista previamente
    * y crea el nuevo usuario en el sistema si todas las validaciones son exitosas.
    */
   @FXML
    public void onRegister() {
        boolean datosValidos = false;
        int tel = 0;
        int card = 0;
        String porque = "Nada";
        
        // Validar que las contraseñas coincidan
        if (fieldPass.getText().equals(fieldPass2.getText())){
            datosValidos = true;
            
            // Validar formato del correo electrónico
            if (!fieldGmail.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                datosValidos = false;
                porque = "Gmail";
                showError("Gmail invalido");
            }
            else{
                datosValidos = true;
                
                // Validar que teléfono y tarjeta sean numéricos
                try {
                    tel = Integer.parseInt(fieldTel.getText());
                    card = Integer.parseInt(fieldCard.getText());
                } catch (NumberFormatException e) {
                    datosValidos = false;
                    showError("Telefono o numero de tarjeta no numericos");
                    porque = "Teléfono o número de tarjeta no válidos (deben ser numéricos)";
                }
            }
        }
        else{
            datosValidos = false;
            porque = "Pass invalida";
            showError("Contraseñas no coinciden");
        }
        
        System.out.println("Datos Validos: " + datosValidos);
        System.out.println(porque);
        
        // Si todas las validaciones son exitosas, crear el usuario
        if (datosValidos) {
            User_ nuevoUser = new User_(
                0,
                fieldUser.getText(),
                fieldPass.getText(),
                fieldGmail.getText(),
                fieldName.getText(),
                fieldSurname.getText(),
                tel,
                card,
                comboGender.getValue()
            );
            
            // Verificar que el usuario no exista previamente
            boolean exists = false;
            if (con.existUser(fieldUser.getText())){
                System.out.println("Usuario ya existente");
                showError("Usuario ya existente");
            }
            else{
                // Insertar el nuevo usuario en la base de datos
                boolean creado = con.insertUser(nuevoUser);
                
                if (creado){
                    System.out.println("Creado correctamente");
                    showSuccess("Usuario creado correctamente");
                    clearFields();                    
                }
            }
        }
    }
   
    /**
     * Maneja el evento del botón "Volver".
     * Cierra la ventana actual de registro y abre la ventana de login.
     */
    @FXML
    private void onBack(){
        System.out.println("Back");
        try {
            // Cargar el FXML de la ventana de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginWindow.fxml"));
            Parent root = loader.load();

            // Crear nueva escena y ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("LogIn");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) btnBack.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showError("No se pudo abrir la ventana de registro.");
        }
    }

    /**
     * Muestra un mensaje de error al usuario.
     * 
     * @param mensaje El mensaje de error a mostrar
     */
    private void showError(String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de éxito al usuario.
     * 
     * @param mensaje El mensaje de éxito a mostrar
     */
    private void showSuccess(String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Limpia todos los campos del formulario de registro.
     */
    private void clearFields(){
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
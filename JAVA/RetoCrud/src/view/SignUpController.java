/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.ImplementsBD;
import model.User_;

/**
 * FXML Controller class
 *
 * @author pablo
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

    
    
    private ImplementsBD bd = new ImplementsBD(); // acceso a BD
    private Controller con = new Controller(); 
    private boolean passwordsVisible = false;    // para mostrar/ocultar contraseñas
    /**
     * Initializes the controller class.
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
   
   @FXML
    public void onRegister() {
        boolean datosValidos=false;
        int tel = 0;
        int card = 0;
        String porque="Nada";
        
        if (fieldPass.getText().equals(fieldPass2.getText())){
            datosValidos=true;
            if (!fieldGmail.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                datosValidos=false;
                porque="Gmail";
                showError("Gmail invalido");
            }
            else{
                datosValidos=true;
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
            datosValidos=false;
            
            porque="Pass invalida";
            showError("Contraseñas no coinciden");
        }
        System.out.println("Datos Validos: "+datosValidos);
        System.out.println(porque);
        
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
        boolean exists=false;
        if (con.existsUser(fieldUser.getText())){
            System.out.println("Usuario ya existente");
            showError("Usuario ya existente");
        }
        else{
          boolean creado = con.insertUser(nuevoUser);
          
          if (creado){
              System.out.println("Creado correctamente");
              showSuccess("Usuario creado correctamente");
              clearFields();
          }
        }
    }
    }
    
    private void showError(String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void showSuccess(String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
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

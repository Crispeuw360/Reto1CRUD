package view;

import controller.Controller;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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

public class AdminViewController implements Initializable {

    @FXML
    private ComboBox<String> comboUsers; 
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

    private User_ user;
    private Map<String, User_> users = new HashMap<>();
    private Controller con = new Controller();
    @FXML
    private Button btnDelete;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Cargar todos los usuarios desde la BD
        users = con.getAllUsers();

        // Rellenar el ComboBox con sus usernames
        comboUsers.getItems().clear();
        comboUsers.getItems().addAll(users.keySet());
        
        btnSave.setDisable(true);
        btnModify.setDisable(true);
        fieldUser.setEditable(false);

        //Detectar cuando el usuario cambia la selección
        comboUsers.setOnAction(e -> onUserSelected());
    }

    private void onUserSelected() {
        String selectedUsername = comboUsers.getValue();
        if (selectedUsername != null) {
            user = users.get(selectedUsername);
            if (user != null) {
                fieldUser.setText(user.getUser_name());
                fieldName.setText(user.getName_());
                fieldSurname.setText(user.getSurname());
                fieldGmail.setText(user.getEmail());
                fieldTel.setText(String.valueOf(user.getTelephone()));
                fieldCard.setText(String.valueOf(user.getCard_no()));
                fieldPass.setText(user.getPasswd());
                fieldPass2.setText(user.getPasswd());
                comboGender.setValue(user.getGender());
                setEditableFields(false);
                btnSave.setDisable(false);
                btnModify.setDisable(false);
                
                
                
            }
        }
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            // Cargar el FXML de la ventana de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginWindow.fxml"));
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
        // implementar modificar
        setEditableFields(true);
        comboGender.getItems().addAll("Male", "Female", "Other");
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

        //Si todo es correcto → actualizamos el objeto y llamamos a BD
        user.setUser_name(fieldUser.getText());
        user.setName_(fieldName.getText());
        user.setSurname(fieldSurname.getText());
        user.setEmail(fieldGmail.getText());
        user.setTelephone(tel);
        user.setCard_no(card);
        user.setGender(comboGender.getValue());
        user.setPasswd(fieldPass.getText());

        boolean ok = con.updateUser(user);

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
private void onDelete(ActionEvent event) {
    String selectedUsername = comboUsers.getValue();
    if (selectedUsername == null || selectedUsername.isEmpty()) {
        showAlert("Por favor, selecciona un usuario para eliminar.", Alert.AlertType.ERROR);
        return;
    }
    
    User_ user = users.get(selectedUsername); // ✅ Use username from combo box
    if (user == null) {
        showAlert("Usuario no encontrado.", Alert.AlertType.ERROR);
        return;
    }
    
    boolean ok = con.deleteUser(user.getUser_name(), user.getUser_code());
    if (ok) {
        showAlert("Usuario eliminado correctamente.", Alert.AlertType.INFORMATION);
        comboUsers.getItems().remove(user.getUser_name());
        users.remove(user.getUser_name()); // Also remove from local map
        clearFields();
        setEditableFields(false);
        btnSave.setDisable(true);
        btnModify.setDisable(true);
    } else {
        showAlert("Error al eliminar el usuario.", Alert.AlertType.ERROR);
    }
}

// Add this helper method to clear fields after deletion
private void clearFields() {
    fieldUser.clear();
    fieldName.clear();
    fieldSurname.clear();
    fieldGmail.clear();
    fieldTel.clear();
    fieldCard.clear();
    fieldPass.clear();
    fieldPass2.clear();
    comboGender.setValue(null);
}
}
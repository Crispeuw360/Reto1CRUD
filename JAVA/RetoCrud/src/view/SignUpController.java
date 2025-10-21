/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

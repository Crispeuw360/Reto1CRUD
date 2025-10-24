/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import controller.Controller;

/**
 * FXML Controller class
 *
 * @author 2dami
 */
public class LoginWindowController implements Initializable {
    private Controller controller;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = new Controller();
    }    

    @FXML
    
    
}

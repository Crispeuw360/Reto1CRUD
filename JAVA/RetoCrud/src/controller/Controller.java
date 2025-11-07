/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Map;

import javafx.stage.Stage;
import model.*;
import view.LoginWindowController;

/**
 *
 * @author 2dami
 */
public class Controller {

    private UserDAO userDAO;

    public Controller() {
        this.userDAO = new ImplementsBD();
    }

    public boolean insertUser(User_ user) {
        return userDAO.insertUser(user);
    }

    public boolean existUser(String username) {
        return userDAO.existUser(username);
    }

    public boolean validatePassword(String username, String password) {
        return userDAO.validatePassword(username, password);
    }

    public User_ getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public boolean updateUser(User_ user) {
        return userDAO.updateUser(user);
    }
    public Map getAllUsers(){
        return userDAO.getAllUsers();
    }
    public boolean isAdmin(String username){
        return userDAO.isAdmin(username);
    }
    public boolean deleteUser(String username){
        return userDAO.deleteUser(username);
    }
    /**
     * Inserta un usuario de manera asíncrona usando un hilo
     * 
     * @param user  Usuario a crear
     * @param stage Stage para cerrar la ventana (puede ser null)
     */
    public void insertUserAsync(User_ user, Stage stage) {
        // Cast a ImplementsBD para acceder al método asíncrono
        ((ImplementsBD) userDAO).insertUserAsync(user, stage);
    }

    /**
     * Realiza el login de manera asíncrona usando un hilo
     * 
     * @param username Nombre de usuario
     * @param password Contraseña
     * @param stage    Stage para cambiar a la ventana de modificación
     * @param loginController Controlador de la ventana de login
     */
    public void loginAsync(String username, String password, Stage stage, LoginWindowController loginController) {
    ((ImplementsBD) userDAO).loginAsync(username, password, stage, loginController);
}
}

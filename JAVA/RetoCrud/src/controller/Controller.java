/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Map;
import model.*;

/**
 * Controlador de la capa de aplicación.
 * Orquesta las operaciones de usuario delegando en {@link UserDAO}.
 * Implementación por defecto: {@link ImplementsBD}.
 * 
 * @author pikain
 */
public class Controller {
    
    private UserDAO userDAO;
    
    /**
     * Crea el controlador con la implementación por defecto de {@link UserDAO}.
     */
    public Controller() {
        this.userDAO = new ImplementsBD();
    }

    /**
     * Inserta un nuevo usuario.
     * @param user datos del usuario
     * @return true si se insertó correctamente
     */
    public boolean insertUser(User_ user){
        return userDAO.insertUser(user);
    }

    /**
     * Comprueba si existe un usuario por nombre.
     * @param username nombre de usuario
     * @return true si existe
     */
    public boolean existUser(String username){
        return userDAO.existUser(username);
    }

    /**
     * Valida la contraseña de un usuario.
     * @param username nombre de usuario
     * @param password contraseña a comprobar
     * @return true si coincide
     */
    public boolean validatePassword(String username, String password){
        return userDAO.validatePassword(username, password);
    }

    /**
     * Obtiene un usuario completo por su nombre.
     * @param username nombre de usuario
     * @return objeto User_ o null si no existe
     */
    public User_ getUserByUsername (String username){
        return userDAO.getUserByUsername(username);
    }

    /**
     * Actualiza los datos de un usuario.
     * @param user usuario con nuevos datos
     * @return true si se actualizó correctamente
     */
    public boolean updateUser (User_ user){
        return userDAO.updateUser(user);
    }

    /**
     * Devuelve todos los usuarios.
     * @return mapa con los usuarios (clave habitual: user_name)
     */
    public Map getAllUsers(){
        return userDAO.getAllUsers();
    }

    /**
     * Indica si el usuario tiene rol de administrador.
     * @param username nombre de usuario
     * @return true si es admin
     */
    public boolean isAdmin(String username){
        return userDAO.isAdmin(username);
    }
}

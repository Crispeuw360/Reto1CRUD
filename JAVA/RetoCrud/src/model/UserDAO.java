/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Map;

/**
 * Interfaz que define las operaciones de acceso a datos (DAO)
 * para los objetos de tipo {@link User_}.
 * 
 * Se utiliza para declarar los métodos que conectan con la base de datos
 * en la clase {@link ImplementsBD}.
 * 
 * @author pikain
 */
public interface UserDAO {
    
    /**
     * Comprueba si existe un usuario con el nombre indicado.
     * @param username nombre de usuario
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean existUser(String username);
    
    /**
     * Inserta un nuevo usuario en la base de datos.
     * @param user objeto User_ con los datos a registrar
     * @return true si la inserción fue correcta, false si falló
     */
    public boolean insertUser(User_ user);
    
    /**
     * Valida la contraseña de un usuario.
     * @param username nombre de usuario
     * @param password contraseña introducida
     * @return true si la contraseña es válida, false si no coincide
     */
    public boolean validatePassword(String username, String password);
    
    /**
     * Devuelve un usuario a partir de su nombre de usuario.
     * @param username nombre de usuario
     * @return objeto User_ con los datos del usuario, o null si no existe
     */
    public User_ getUserByUsername(String username);
    
    /**
     * Actualiza los datos de un usuario existente.
     * @param user objeto User_ con los nuevos datos
     * @return true si la actualización fue correcta, false en caso contrario
     */
    public boolean updateUser(User_ user);
    
    /**
     * Devuelve todos los usuarios almacenados en la base de datos.
     * @return mapa con los usuarios (clave: nombre de usuario)
     */
    public Map getAllUsers();
    
    /**
     * Comprueba si el usuario tiene rol de administrador.
     * @param username nombre de usuario
     * @return true si el usuario es administrador, false si es usuario normal
     */
    public boolean isAdmin(String username);
}

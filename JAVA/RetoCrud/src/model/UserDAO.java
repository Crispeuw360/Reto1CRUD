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
 * Se utiliza para declarar los métodos que conectan con la base de datos
 * en la clase {@link ImplementsBD}.
 * 
 * @author pikain
 */
public interface UserDAO {
    /**
     * Verifica si existe un usuario con el nombre de usuario proporcionado.
     * 
     * @param username Nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean existUser(String username);

    /**
     * Inserta un nuevo usuario en la base de datos.
     * 
     * @param user Objeto {@link User_} que contiene los datos del usuario a insertar.
     * @return true si la inserción es exitosa, false en caso contrario.
     */
    public boolean insertUser(User_ user);
    
    /**
     * Valida la contraseña de un usuario.
     * 
     * @param username Nombre de usuario para validar.
     * @param password Contraseña a validar.
     * @return true si la contraseña es correcta, false en caso contrario.
     */
    public boolean validatePassword(String username, String password);
    
    /**
     * Obtiene un usuario por su nombre de usuario.
     * 
     * @param username Nombre de usuario del usuario a obtener.
     * @return Objeto {@link User_} que contiene los datos del usuario, o null si no se encuentra.
     */
    public User_ getUserByUsername(String username);
    
    /**
     * Actualiza los datos de un usuario en la base de datos.
     * 
     * @param user Objeto {@link User_} que contiene los datos del usuario a actualizar.
     * @return true si la actualización es exitosa, false en caso contrario.
     */
    public boolean updateUser(User_ user);

    /**
     * Obtiene todos los usuarios almacenados en la base de datos.
     * 
     * @return Mapa que contiene los usuarios, donde la clave es el nombre de usuario y el valor es el objeto {@link User_}.
     */
    public Map<String, User_> getAllUsers();
    
    /**
     * Verifica si un usuario es administrador.
     * 
     * @param username Nombre de usuario del usuario a verificar.
     * @return true si el usuario es administrador, false en caso contrario.
     */
    public boolean isAdmin(String username);

    /**
     * Elimina un usuario de la base de datos.
     * 
     * @param username Nombre de usuario del usuario a eliminar.
     * @param profile_code Código del perfil del usuario a eliminar.
     * @return true si la eliminación es exitosa, false en caso contrario.
     */
    public boolean deleteUser(String username,int profile_code);
}

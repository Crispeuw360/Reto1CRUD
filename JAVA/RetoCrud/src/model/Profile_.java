/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Clase abstracta que representa un perfil genérico de usuario.
 * Contiene los datos comunes a todos los tipos de usuario del sistema.
 * 
 * @author pikain
 */
public  abstract class Profile_ {
    private int user_code;
    private String user_name;
    private String passwd;
    private String email;
    private String name_;
    private String Surname;
    private int Telephone;

     /**
     * Constructor vacío de la clase Profile_.
     */
    public Profile_() {
    }

    /**
     * Constructor completo de la clase Profile_.
     * Permite crear un perfil con todos sus datos personales.
     * 
     * @param user_code Código numérico identificativo del usuario.
     * @param user_name Nombre de usuario para el inicio de sesión.
     * @param passwd Contraseña del usuario.
     * @param email Correo electrónico asociado al usuario.
     * @param name_ Nombre real del usuario.
     * @param Surname Apellido del usuario.
     * @param Telephone Teléfono de contacto del usuario.
     */
    public Profile_(int user_code, String user_name, String passwd, String email, String name_, String Surname, int Telephone) {
        this.user_code = user_code;
        this.user_name = user_name;
        this.passwd = passwd;
        this.email = email;
        this.name_ = name_;
        this.Surname = Surname;
        this.Telephone = Telephone;
    }
    
    /**
     * Obtiene el código del usuario.
     * 
     * @return Código numérico identificativo del usuario.
     */
    public int getUser_code() {
        return user_code;
    }

    /**
     * Establece el código del usuario.
     * 
     * @param user_code Código numérico identificativo del usuario.
     */
    public void setUser_code(int user_code) {
        this.user_code = user_code;
    }

    /**
     * Obtiene el nombre de usuario.
     * 
     * @return Nombre de usuario para el inicio de sesión.
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * Establece el nombre de usuario.
     * 
     * @param user_name Nombre de usuario para el inicio de sesión.
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return Contraseña del usuario.
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * @param passwd Contraseña del usuario.
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * 
     * @return Correo electrónico asociado al usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param email Correo electrónico asociado al usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el nombre real del usuario.
     * 
     * @return Nombre real del usuario.
     */
    public String getName_() {
        return name_;
    }

    /**
     * Establece el nombre real del usuario.
     * 
     * @param name_ Nombre real del usuario.
     */
    public void setName_(String name_) {
        this.name_ = name_;
    }

    /**
     * Obtiene el apellido del usuario.
     * 
     * @return Apellido del usuario.
     */
    public String getSurname() {
        return Surname;
    }
    
    /**
     * Establece el apellido del usuario.
     * 
     * @param Surname Apellido del usuario.
     */
    public void setSurname(String Surname) {
        this.Surname = Surname;
    }

    /**
     * Obtiene el teléfono del usuario.
     * 
     * @return Teléfono de contacto del usuario.
     */
    public int getTelephone() {
        return Telephone;
    }

    /**
     * Establece el teléfono del usuario.
     * 
     * @param Telephone Teléfono de contacto del usuario.
     */
    public void setTelephone(int Telephone) {
        this.Telephone = Telephone;
    }

    /**
     * Devuelve una representación textual del objeto Profile_.
     * 
     * @return Cadena con la información principal del perfil.
     */
    @Override
    public String toString() {
        return "Profile_ [user_code=" + user_code + ", user_name=" + user_name + ", passwd=" + passwd + ", email=" + email + ", name_=" + name_ + ", Surname=" + Surname + ", Telephone=" + Telephone + "]";
    }
}

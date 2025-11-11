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
public abstract class Profile_ {
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
     * Constructor con todos los atributos del perfil.
     * @param user_code código identificativo del usuario
     * @param user_name nombre de usuario
     * @param passwd contraseña
     * @param email correo electrónico
     * @param name_ nombre real
     * @param Surname apellido
     * @param Telephone teléfono de contacto
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
     * Obtiene el código identificativo del usuario.
     * @return el código del usuario
     */
    public int getUser_code() {
        return user_code;
    }

    /**
     * Establece el código identificativo del usuario.
     * @param user_code el código del usuario a establecer
     */
    public void setUser_code(int user_code) {
        this.user_code = user_code;
    }

    /**
     * Obtiene el nombre de usuario para login.
     * @return el nombre de usuario
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * Establece el nombre de usuario para login.
     * @param user_name el nombre de usuario a establecer
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return la contraseña del usuario
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * Establece la contraseña del usuario.
     * @param passwd la contraseña a establecer
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * @return el email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * @param email el email a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el nombre real del usuario.
     * @return el nombre del usuario
     */
    public String getName_() {
        return name_;
    }

    /**
     * Establece el nombre real del usuario.
     * @param name_ el nombre a establecer
     */
    public void setName_(String name_) {
        this.name_ = name_;
    }

    /**
     * Obtiene el apellido del usuario.
     * @return el apellido del usuario
     */
    public String getSurname() {
        return Surname;
    }

    /**
     * Establece el apellido del usuario.
     * @param Surname el apellido a establecer
     */
    public void setSurname(String Surname) {
        this.Surname = Surname;
    }

    /**
     * Obtiene el número de teléfono del usuario.
     * @return el teléfono del usuario
     */
    public int getTelephone() {
        return Telephone;
    }

    /**
     * Establece el número de teléfono del usuario.
     * @param Telephone el teléfono a establecer
     */
    public void setTelephone(int Telephone) {
        this.Telephone = Telephone;
    }

    /**
     * Devuelve los datos principales del perfil en formato texto.
     * @return información del perfil como cadena
     */
    @Override
    public String toString() {
        return "Profile_ [user_code=" + user_code + ", user_name=" + user_name + ", passwd=" + passwd + ", email=" + email + ", name_=" + name_ + ", Surname=" + Surname + ", Telephone=" + Telephone + "]";
    }
}
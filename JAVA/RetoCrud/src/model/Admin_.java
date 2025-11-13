/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Clase que representa un perfil de tipo Administrador dentro del sistema.
 * Hereda de la clase {@link Profile_} e incluye un atributo adicional
 * para identificar la cuenta actual gestionada.
 * 
 * @author pikain
 * @version 1.0
 */
public class Admin_ extends Profile_ {
    /** Nombre o identificador de la cuenta actual asociada al administrador. */
    private String Current_account;

    /**
     * Constructor vacío de la clase Admin_.
     * Inicializa un objeto sin establecer valores iniciales.
     */
    public Admin_() {

    }

    /**
     * Constructor completo de la clase Admin_.
     * Permite crear un administrador con todos sus datos personales y de cuenta.
     * 
     * @param user_code       Código numérico identificativo del usuario.
     * @param user_name       Nombre de usuario para el inicio de sesión.
     * @param passwd          Contraseña del usuario.
     * @param email           Correo electrónico asociado al usuario.
     * @param name_           Nombre real del usuario.
     * @param Surname         Apellido del usuario.
     * @param Telephone       Teléfono de contacto del usuario.
     * @param Current_account Cuenta actual gestionada por el administrador.
     */
    public Admin_(int user_code, String user_name, String passwd, String email, String name_, String Surname,
            int Telephone, String Current_account) {
        super(user_code, user_name, passwd, email, name_, Surname, Telephone);
        this.Current_account = Current_account;
    }

    /**
     * Devuelve la cuenta actual del administrador.
     * 
     * @return Nombre o identificador de la cuenta actual.
     */
    public String getCurrent_account() {
        return Current_account;
    }

    /**
     * Establece la cuenta actual del administrador.
     * 
     * @param Current_account Nueva cuenta que gestionará el administrador.
     */
    public void setCurrent_account(String Current_account) {
        this.Current_account = Current_account;
    }

    /**
     * Devuelve una representación textual del objeto Admin_.
     * 
     * @return Cadena con la información principal del administrador.
     */
    @Override
    public String toString() {
        return "Admin_ [Current_account=" + Current_account + "]";
    }
}

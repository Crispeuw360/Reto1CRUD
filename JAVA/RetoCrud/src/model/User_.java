/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
* Clase que representa a un usuario del sistema.
* Hereda los datos generales de {@link Profile_} y añade información
* específica como el número de tarjeta y el género.
* 
* @author pikain
*/
public class User_ extends Profile_ {
    private int card_no;
    private String gender;

    /**
     * Constructor vacío de la clase User_.
     * Inicializa un objeto sin establecer valores iniciales.
     */
    public User_() {
    }

    /**
     * Constructor completo de la clase User_.
     * Permite crear un usuario con todos sus datos personales y de cuenta.
     * 
     * @param user_code       Código numérico identificativo del usuario.
     * @param user_name       Nombre de usuario para el inicio de sesión.
     * @param passwd          Contraseña del usuario.
     * @param email           Correo electrónico asociado al usuario.
     * @param name_           Nombre real del usuario.
     * @param Surname         Apellido del usuario.
     * @param Telephone       Teléfono de contacto del usuario.
     * @param card_no         Número de tarjeta del usuario.
     * @param gender          Género del usuario.
     */
    public User_(int user_code, String user_name, String passwd, String email, String name_, String Surname, int Telephone, int card_no, String gender) {
        super(user_code, user_name, passwd, email, name_, Surname, Telephone);
        this.card_no = card_no;
        this.gender = gender;
    }

    /**
     * Obtiene el número de tarjeta del usuario.
     * 
     * @return Número de tarjeta del usuario.
     */
    public int getCard_no() {
        return card_no;
    }

    /**
     * Establece el número de tarjeta del usuario.
     * 
     * @param card_no Número de tarjeta del usuario.
     */
    public void setCard_no(int card_no) {
        this.card_no = card_no;
    }

    /**
     * Obtiene el género del usuario.
     * 
     * @return Género del usuario.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Establece el género del usuario.
     * 
     * @param gender Género del usuario.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * Devuelve una representación textual del objeto User_.
     * 
     * @return Cadena con la información principal del usuario.
     */
    @Override
    public String toString() {
        return "User_ [card_no=" + card_no + ", gender=" + gender + "]";
    }
    
}

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
     */
    public User_() {
    }

    /**
     * Constructor con todos los atributos del usuario.
     * 
     * @param user_code código identificativo del usuario
     * @param user_name nombre de usuario
     * @param passwd contraseña del usuario
     * @param email correo electrónico
     * @param name_ nombre real del usuario
     * @param Surname apellido del usuario
     * @param Telephone número de teléfono
     * @param card_no número de tarjeta del usuario
     * @param gender género del usuario
     */
    public User_(int user_code, String user_name, String passwd, String email, String name_, String Surname, int Telephone, int card_no, String gender) {
        super(user_code, user_name, passwd, email, name_, Surname, Telephone);
        this.card_no = card_no;
        this.gender = gender;
    }

    /**
     * Obtiene el número de tarjeta del usuario.
     * @return el número de tarjeta
     */
    public int getCard_no() {
        return card_no;
    }

    /**
     * Establece el número de tarjeta del usuario.
     * @param card_no el número de tarjeta a establecer
     */
    public void setCard_no(int card_no) {
        this.card_no = card_no;
    }

    /**
     * Obtiene el género del usuario.
     * @return el género del usuario
     */
    public String getGender() {
        return gender;
    }

    /**
     * Establece el género del usuario.
     * @param gender el género a establecer
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * Devuelve la información principal del usuario en formato texto.
     * @return cadena con los datos del usuario
     */
    @Override
    public String toString() {
        return "User_ [card_no=" + card_no + ", gender=" + gender + "]";
    }
}
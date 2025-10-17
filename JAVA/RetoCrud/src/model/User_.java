/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author 2dami
 */
public class User_ extends Profile_ {
    private int card_no;
    private String gender;

    public User_() {
    }

    public User_(int user_code, String user_name, String passwd, String email, String name_, String Surname, int Telephone, int card_no, String gender) {
        super(user_code, user_name, passwd, email, name_, Surname, Telephone);
        this.card_no = card_no;
        this.gender = gender;
    }

    public int getCard_no() {
        return card_no;
    }

    public void setCard_no(int card_no) {
        this.card_no = card_no;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    @Override
    public String toString() {
        return "User_ [card_no=" + card_no + ", gender=" + gender + "]";
    }
    
}

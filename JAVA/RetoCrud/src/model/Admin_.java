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
public class Admin_ extends Profile_ {
    private String Current_account;
    
    public Admin_() {
        
    }
    
    public Admin_(int user_code, String user_name, String passwd, String email, String name_, String Surname, int Telephone, String Current_account) {
        super(user_code, user_name, passwd, email, name_, Surname, Telephone);
        this.Current_account = Current_account;
    }
    
    public String getCurrent_account() {
        return Current_account;
    }
    
    public void setCurrent_account(String Current_account) {
        this.Current_account = Current_account;
    }
    
    @Override
    public String toString() {
        return "Admin_ [Current_account=" + Current_account + "]";
    }
}

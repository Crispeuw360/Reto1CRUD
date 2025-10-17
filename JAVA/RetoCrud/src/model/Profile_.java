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
public  abstract class Profile_ {
    private int user_code;
    private String user_name;
    private String passwd;
    private String email;
    private String name_;
    private String Surname;
    private int Telephone;

    public Profile_() {
    }

    public Profile_(int user_code, String user_name, String passwd, String email, String name_, String Surname, int Telephone) {
        this.user_code = user_code;
        this.user_name = user_name;
        this.passwd = passwd;
        this.email = email;
        this.name_ = name_;
        this.Surname = Surname;
        this.Telephone = Telephone;
    }
    

    public int getUser_code() {
        return user_code;
    }

    public void setUser_code(int user_code) {
        this.user_code = user_code;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String Surname) {
        this.Surname = Surname;
    }

    public int getTelephone() {
        return Telephone;
    }

    public void setTelephone(int Telephone) {
        this.Telephone = Telephone;
    }

    @Override
    public String toString() {
        return "Profile_ [user_code=" + user_code + ", user_name=" + user_name + ", passwd=" + passwd + ", email=" + email + ", name_=" + name_ + ", Surname=" + Surname + ", Telephone=" + Telephone + "]";
    }
}

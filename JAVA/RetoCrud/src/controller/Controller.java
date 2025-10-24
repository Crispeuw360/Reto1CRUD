/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import model.*;

/**
 *
 * @author 2dami
 */
public class Controller {
    
    private UserDAO userDAO;
    
    public Controller() {
        this.userDAO = new ImplementsBD();
    }
    
    
}

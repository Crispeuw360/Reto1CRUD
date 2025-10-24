/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
<<<<<<< HEAD

import model.WorkerDAO;

/**
 *
 * @author pablo
 */
public class Controller {
    WorkerDAO dao = (WorkerDAO) new model.ImplementsBD();
    
     
     
=======
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
    
    public boolean checkUser(String username, String password) {
        return userDAO.checkUser(username, password);
    }
    
    public User_ showUser(String username) {
        return userDAO.showUser(username);
    }
>>>>>>> 9ed9ad2dc51612e4fd37c0af65048e0cad26c4b2
}

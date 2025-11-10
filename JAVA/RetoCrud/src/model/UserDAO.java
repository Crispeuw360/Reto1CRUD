/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Map;

import java.util.Map;

/**
 *
 * @author 2dami
 */
public interface UserDAO {
     public boolean existUser(String username);
    
    public boolean insertUser(User_ user);
    
    public boolean validatePassword(String username, String password);
    
    public User_ getUserByUsername(String username);
    
    public boolean updateUser(User_ user);

    public Map<String, User_> getAllUsers();
    
    public boolean isAdmin(String username);

    public boolean deleteUser(String username,int profile_code);
}

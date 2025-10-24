/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *
 * @author pablo
 */
public class ImplementsBD implements UserDAO {
    private Connection con;
    private PreparedStatement stmt;
    
    private ResourceBundle configFile;
    private String driverBD;
    private String urlBD;
    private String userBD;
    private String passwordBD;
    
    
    
    
    public ImplementsBD(){
        this.configFile = ResourceBundle.getBundle("model.configClase");
        this.driverBD = this.configFile.getString("Driver");
        this.urlBD = this.configFile.getString("Conn");
        this.userBD = this.configFile.getString("DBUser");
        this.passwordBD = this.configFile.getString("DBPass");
    }
    
     private void openConnection() {
        try {
            con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD);
        } catch (SQLException e) {
            System.out.println("Error al intentar abrir la BD");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     private void closeConnection() {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión con la BD");
            e.printStackTrace();
        }
    }
     public boolean existsUser(String username) {
        boolean exists = false;

        try {
            openConnection();
            String sql = "SELECT COUNT(*) FROM Profile_ WHERE user_name = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error al comprobar si el usuario existe");
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return exists;
    }

     
     public boolean insertUser(User_ user) {
        boolean inserted = false;
        try {
            openConnection();

            // Paso 1: Insertar en Profile_
            String sqlProfile = "INSERT INTO Profile_ (user_name, passwd, email, name_, Surname, Telephone) VALUES (?,?,?,?,?,?)";
            stmt = con.prepareStatement(sqlProfile, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUser_name());
            stmt.setString(2, user.getPasswd());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getName_());
            stmt.setString(5, user.getSurname());
            stmt.setInt(6, user.getTelephone());
            stmt.executeUpdate();

            // Obtener el ID generado
            ResultSet rs = stmt.getGeneratedKeys();
            int profileCode = 0;
            if (rs.next()) {
                profileCode = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            // Paso 2: Insertar en User_
            String sqlUser = "INSERT INTO User_ (Profile_code, card_no, gender) VALUES (?,?,?)";
            stmt = con.prepareStatement(sqlUser);
            stmt.setInt(1, profileCode);
            stmt.setInt(2, user.getCard_no());
            stmt.setString(3, user.getGender());

            inserted = stmt.executeUpdate() > 0;
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return inserted;
    }

     
}

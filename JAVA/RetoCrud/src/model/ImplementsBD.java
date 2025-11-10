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
import java.util.HashMap;
import java.util.Map;
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


    public boolean existUser(String username) {
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
            System.out.println(" Error al comprobar si el usuario existe");
            e.printStackTrace();
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
        } 
        
        return inserted;
    }
     
    public boolean validatePassword(String username, String password) {
        boolean valid = false;
        try {
            openConnection();
            String sql = "SELECT passwd FROM Profile_ WHERE user_name = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPass = rs.getString("passwd");
                if (storedPass.equals(password)) {
                    valid = true;
                }
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(" Error al validar contraseña");
            e.printStackTrace();
        } 
        return valid;
    }
    public User_ getUserByUsername(String username) {
        User_ user = null;

        try {
            openConnection();

            //Unir las dos tablas por Profile_code / user_code
            String sql = "SELECT p.user_code, p.user_name, p.passwd, p.email, p.name_, p.Surname, p.Telephone, "
                       + "u.card_no, u.gender "
                       + "FROM Profile_ p "
                       + "INNER JOIN User_ u ON p.user_code = u.Profile_code "
                       + "WHERE p.user_name = ?";

            stmt = con.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                user = new User_();
                user.setUser_code(rs.getInt("user_code"));
                user.setUser_name(rs.getString("user_name"));
                user.setPasswd(rs.getString("passwd"));
                user.setEmail(rs.getString("email"));
                user.setName_(rs.getString("name_"));
                user.setSurname(rs.getString("Surname"));
                user.setTelephone(rs.getInt("Telephone"));
                user.setCard_no(rs.getInt("card_no"));
                user.setGender(rs.getString("gender"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener el usuario completo por nombre");
            e.printStackTrace();
        }

        return user;
    }
    public boolean updateUser(User_ user) {
        boolean updated = false;

        try {
            openConnection();

            // ctualizamos la tabla Profile_
            String sqlProfile = "UPDATE Profile_ SET user_name=?, passwd=?, email=?, name_=?, Surname=?, Telephone=? WHERE user_code=?";
            stmt = con.prepareStatement(sqlProfile);
            stmt.setString(1, user.getUser_name());
            stmt.setString(2, user.getPasswd());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getName_());
            stmt.setString(5, user.getSurname());
            stmt.setInt(6, user.getTelephone());
            stmt.setInt(7, user.getUser_code());
            stmt.executeUpdate();
            stmt.close();

            // Actualizamos la tabla User_
            String sqlUser = "UPDATE User_ SET card_no=?, gender=? WHERE Profile_code=?";
            stmt = con.prepareStatement(sqlUser);
            stmt.setInt(1, user.getCard_no());
            stmt.setString(2, user.getGender());
            stmt.setInt(3, user.getUser_code());
            updated = stmt.executeUpdate() > 0;

            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar el usuario");
            e.printStackTrace();
        }

        return updated;
    }
    
    public Map<String, User_> getAllUsers() {
        Map<String, User_> usersMap = new HashMap<>();

        try {
            openConnection();

            // Consulta conjunta Profile_ + User_
            String sql = "SELECT p.user_code, p.user_name, p.passwd, p.email, p.name_, p.Surname, p.Telephone, "
                       + "u.card_no, u.gender "
                       + "FROM Profile_ p "
                       + "INNER JOIN User_ u ON p.user_code = u.Profile_code";

            stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Recorremos resultados y los metemos en el mapa
            while (rs.next()) {
                User_ user = new User_();
                user.setUser_code(rs.getInt("user_code"));
                user.setUser_name(rs.getString("user_name"));
                user.setPasswd(rs.getString("passwd"));
                user.setEmail(rs.getString("email"));
                user.setName_(rs.getString("name_"));
                user.setSurname(rs.getString("Surname"));
                user.setTelephone(rs.getInt("Telephone"));
                user.setCard_no(rs.getInt("card_no"));
                user.setGender(rs.getString("gender"));

                // Añadir al HashMap
                usersMap.put(user.getUser_name(), user);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener todos los usuarios");
            e.printStackTrace();
        }

        return usersMap;
    }
    public boolean isAdmin(String username) {
        boolean admin = false;

        try {
            openConnection();

            // 🔹 Obtener el ID del usuario en Profile_
            String sql = "SELECT user_code FROM Profile_ WHERE user_name = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            int userCode = -1;
            if (rs.next()) {
                userCode = rs.getInt("user_code");
            }
            rs.close();
            stmt.close();

            // 🔹 Si existe, mirar si aparece en la tabla Admin_
            if (userCode != -1) {
                String sql2 = "SELECT COUNT(*) FROM Admin_ WHERE Profile_code = ?";
                stmt = con.prepareStatement(sql2);
                stmt.setInt(1, userCode);
                ResultSet rs2 = stmt.executeQuery();

                if (rs2.next() && rs2.getInt(1) > 0) {
                    admin = true;
                }

                rs2.close();
                stmt.close();
            }

        } catch (SQLException e) {
            System.out.println("❌ Error comprobando si el usuario es administrador");
            e.printStackTrace();
        }

        return admin;
    }





     
}

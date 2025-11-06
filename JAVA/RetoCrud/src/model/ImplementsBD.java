/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author pablo
 */
public class ImplementsBD implements UserDAO {
    private Connection con;
    private PreparedStatement stmt;

    public ImplementsBD() {
        // No necesitamos cargar configuración desde properties
        // porque el pool ya tiene la configuración hardcodeada
    }

    private void openConnection() {
        try {
            // Usar directamente el pool de conexiones
            con = ConexionPoolDBCP.getConnection();
        } catch (SQLException e) {
            System.out.println("Error al intentar abrir la BD desde el pool");
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
            System.out.println("❌ Error al validar contraseña");
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return valid;
    }

    public User_ getUserByUsername(String username) {
        User_ user = null;

        try {
            openConnection();

            // Unir las dos tablas por Profile_code / user_code
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
            System.out.println("❌ Error al obtener el usuario completo por nombre");
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return user;
    }

    public boolean updateUser(User_ user) {
        boolean updated = false;

        try {
            openConnection();

            // Actualizamos la tabla Profile_
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
        } finally {
            closeConnection();
        }

        return updated;
    }

    // Método para cerrar la conexión
    private void closeConnection() {
        try {
            if (con != null) {
                con.close(); // Esto devuelve la conexión al pool
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión");
            e.printStackTrace();
        }
    }
}
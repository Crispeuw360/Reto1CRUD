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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author PIKAIN
 * @version 1.0
 *
 */
public class ImplementsBD implements UserDAO {

    private Connection con;
    private ConexionPoolStack pool = new ConexionPoolStack("jdbc:mysql://localhost:3306/retocrud?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true", "root", "abcd*1234", 3);
    private PreparedStatement stmt;
    private ThreadConexion threadConexion;

    //All sql 
    private final String sqlProfile = "INSERT INTO Profile_ (user_name, passwd, email, name_, Surname, Telephone) VALUES (?,?,?,?,?,?)";
    private final String sqlUser = "INSERT INTO User_ (Profile_code, card_no, gender) VALUES (?,?,?)";
    private final String sqlExistUser = "SELECT COUNT(*) FROM Profile_ WHERE user_name = ?";
    private final String sqlValidatePassword = "SELECT passwd FROM Profile_ WHERE user_name = ?";
    private final String sqlGetUserByUsername = "SELECT p.user_code, p.user_name, p.passwd, p.email, p.name_, p.Surname, p.Telephone, "
            + "u.card_no, u.gender "
            + "FROM Profile_ p "
            + "INNER JOIN User_ u ON p.user_code = u.Profile_code "
            + "WHERE p.user_name = ?";
    private final String sqlAdmin = "SELECT user_code FROM Profile_ WHERE user_name = ?";
    private final String sqlAdmin2 = "SELECT COUNT(*) FROM Admin_ WHERE Profile_code = ?";
    private final String sqlUpdateUser = " UPDATE User_ SET card_no=?, gender=? WHERE Profile_code=?";
    private final String sqlUpdateProfile = "UPDATE Profile_ SET passwd = ?, email = ?, name_ = ?, Surname = ?, Telephone = ? WHERE user_name = ?";
    private final String sqlDeleteProfile = "DELETE FROM Profile_ WHERE user_name = ? ";
    private final String sqlListUsers = "SELECT p.user_code, p.user_name, p.passwd, p.email, p.name_, p.Surname, p.Telephone, "
            + "u.card_no, u.gender "
            + "FROM Profile_ p "
            + "INNER JOIN User_ u ON p.user_code = u.Profile_code";

    public ImplementsBD() {

    }

    /**
     * Abre una conexión con la base de datos.
     *
     * @param ConexionPoolDBCP
     * @throws SQLException Si ocurre un error al abrir la conexión.
     */
    private void openConnection() {
        con = null;
        try {
            con = pool.getConnection();
            if (con != null && !con.isClosed()) {
                System.out.println(" Conexión abierta exitosamente");
            } else {
                System.out.println(" Conexión nula o cerrada obtenida del pool");
                con = null;
            }
        } catch (SQLException e) {
            con = null;
        }
    }

    /**
     * Verifica si existe un usuario con el nombre de usuario proporcionado.
     *
     * @param username Nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean existUser(String username) {
        boolean exists = false;

        try {
            openConnection();
            if (con == null) {
                System.out.println(" No hay conexiones disponibles en este momento. Reintentando más tarde...");
                return false; // NO mostrar error, solo retornar false silenciosamente
            }

            stmt = con.prepareStatement(sqlExistUser);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // CAPTURAR ERROR SILENCIOSAMENTE sin printStackTrace
            if (e.getMessage().contains("Timeout")) {
                System.out.println(" Timeout de conexión - El sistema está ocupado. Intenta nuevamente en 30 segundos.");
            } else {
                System.out.println(" Error en existUser: " + e.getMessage());
            }
        } finally {
            if (con != null) {
                pool.releaseConnection(con);
            }
        }

        return exists;
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param user Objeto {@link User_} que contiene los datos del usuario a
     * insertar.
     * @return true si la inserción es exitosa, false en caso contrario.
     */
    public boolean insertUser(User_ user) {
        boolean inserted = false;
        threadConexion = new ThreadConexion(con);
        threadConexion.start();
        try {
            openConnection();

            // Paso 1: Insertar en Profile_
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
            stmt = con.prepareStatement(sqlUser);
            stmt.setInt(1, profileCode);
            stmt.setInt(2, user.getCard_no());
            stmt.setString(3, user.getGender());

            inserted = stmt.executeUpdate() > 0;
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                pool.releaseConnection(con);
            }
        }

        return inserted;
    }

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param username Nombre de usuario del usuario a eliminar.
     * @param profile_code Código del perfil del usuario a eliminar.
     * @return true si la eliminación es exitosa, false en caso contrario.
     */
    public boolean deleteUser(String username, int profile_code) {
        boolean deleted = false;
        threadConexion = new ThreadConexion(con);
        threadConexion.start();

        try {
            openConnection();
            if (con == null) {
                System.out.println(" No hay conexiones disponibles para eliminar usuario");
                return false; // Sin error en rojo
            }

            stmt = con.prepareStatement(sqlDeleteProfile);
            stmt.setString(1, username);
            deleted = stmt.executeUpdate() > 0;
            stmt.close();
        } catch (SQLException e) {
            System.out.println(" Error al eliminar el usuario");

        } finally {
            if (con != null) {
                pool.releaseConnection(con);
            }
        }

        return deleted;
    }

    /**
     * Valida la contraseña de un usuario.
     *
     * @param username Nombre de usuario para validar.
     * @param password Contraseña a validar.
     * @return true si la contraseña es correcta, false en caso contrario.
     */
    public boolean validatePassword(String username, String password) {
        boolean valid = false;

        try {
            openConnection();
            if (con == null) {
                System.out.println(" No hay conexiones disponibles para validar password");
                return false; // Sin error en rojo
            }

            stmt = con.prepareStatement(sqlValidatePassword);
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
            // ✅ Sin error en rojo
            if (e.getMessage().contains("Timeout")) {
                System.out.println(" Timeout validando password - Sistema ocupado");
            } else {
                System.out.println(" Error en validatePassword: " + e.getMessage());
            }
        } finally {
            if (con != null) {
                pool.releaseConnection(con);
            }
        }
        return valid;
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario del usuario a obtener.
     * @return Objeto {@link User_} que contiene los datos del usuario, o null
     * si no se encuentra.
     */
    public User_ getUserByUsername(String username) {
        User_ user = null;

        try {
            openConnection();
            if (con == null) {
                System.out.println("⏳ No hay conexiones disponibles para obtener usuario");
                return null; // ❌ Sin error en rojo
            }

            stmt = con.prepareStatement(sqlGetUserByUsername);
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
            // ✅ Sin error en rojo
            if (e.getMessage().contains("Timeout")) {
                System.out.println(" Timeout obteniendo usuario - Sistema ocupado");
            } else {
                System.out.println(" Error en getUserByUsername: " + e.getMessage());
            }
        } finally {
            if (con != null) {
                pool.releaseConnection(con);
            }
        }

        return user;
    }

    /**
     * Actualiza los datos de un usuario en la base de datos.
     *
     * @param user Objeto {@link User_} que contiene los datos del usuario a
     * actualizar.
     * @return true si la actualización es exitosa, false en caso contrario.
     */
    public boolean updateUser(User_ user) {
        boolean updated = false;
        threadConexion = new ThreadConexion(con);
        threadConexion.start();

        try {
            openConnection();

            // Actualizamos la tabla Profile_
            stmt = con.prepareStatement(sqlUpdateProfile);
            stmt.setString(1, user.getPasswd());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getName_());
            stmt.setString(4, user.getSurname());
            stmt.setInt(5, user.getTelephone());
            stmt.setString(6, user.getUser_name());
            stmt.executeUpdate();
            stmt.close();

            // Actualizamos la tabla User_
            stmt = con.prepareStatement(sqlUpdateUser);
            stmt.setInt(1, user.getCard_no());
            stmt.setString(2, user.getGender());
            stmt.setInt(3, user.getUser_code());
            updated = stmt.executeUpdate() > 0;

            stmt.close();
        } catch (SQLException e) {
            System.out.println(" Error al actualizar el usuario");

        } finally {
            if (con != null) {
                pool.releaseConnection(con);
            }
        }

        return updated;
    }

    /**
     * Obtiene todos los usuarios almacenados en la base de datos.
     *
     * @return Mapa que contiene los usuarios, donde la clave es el nombre de
     * usuario y el valor es el objeto {@link User_}.
     */
    public Map<String, User_> getAllUsers() {
        Map<String, User_> usersMap = new HashMap<>();
        threadConexion = new ThreadConexion(con);
        threadConexion.start();

        try {
            openConnection();

            // Consulta conjunta Profile_ + User_
            stmt = con.prepareStatement(sqlListUsers);
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
            System.out.println(" Error al eliminar el usuario");

        } finally {
            if (con != null) {
                pool.releaseConnection(con);
            }
        }

        return usersMap;
    }

    /**
     * Verifica si un usuario es administrador.
     *
     * @param username Nombre de usuario del usuario a verificar.
     * @return true si el usuario es administrador, false en caso contrario.
     */
    public boolean isAdmin(String username) {
        boolean admin = false;

        try {
            openConnection();

            // 🔹 Obtener el ID del usuario en Profile_
            stmt = con.prepareStatement(sqlAdmin);
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
                stmt = con.prepareStatement(sqlAdmin2);
                stmt.setInt(1, userCode);
                ResultSet rs2 = stmt.executeQuery();

                if (rs2.next() && rs2.getInt(1) > 0) {
                    admin = true;
                }

                rs2.close();
                stmt.close();
            }

        } catch (SQLException e) {
            System.out.println(" Error comprobando si el usuario es administrador");

        } finally {
            if (con != null) {
                pool.releaseConnection(con);
            }
        }

        return admin;
    }

}

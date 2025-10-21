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
import java.util.ResourceBundle;

/**
 *
 * @author 2dami
 */
public class ImplementsBD implements UserDAO{
    
    private Connection con;
        private PreparedStatement stmt;
        
        private ResourceBundle configFile;
        private String driverBD;
        private String urlBD;
        private String userBD;
        private String passwordBD;
        
        /** Consulta para recuperar un usuario por nombre. */
       final String SQLGETUSER ="SELECT USER_CODE, USER_NAME, PASSWD, EMAIL, NAME_, SURNAME, TELEPHONE, CARD_NO, GENDER FROM USUARIO WHERE USER_NAME = ?";
       /** Consulta para validar credenciales de usuario. */
       final String SQLCHECK ="SELECT 1 FROM USUARIO WHERE USER_NAME=? AND PASSWD=?";
        
        
        /**
         * Constructor que inicializa la configuración de conexión a BD
         * leyendo valores del fichero {@code configClase.properties}.
         */
        public ImplementsBD() {
		this.configFile = ResourceBundle.getBundle("configClase");
		this.driverBD = this.configFile.getString("Driver");
		this.urlBD = this.configFile.getString("Conn");
		this.userBD = this.configFile.getString("DBUser");
		this.passwordBD = this.configFile.getString("DBPass");
	}
        
        /**
         * Abre una conexión JDBC con la base de datos usando la configuración cargada.
         * En caso de error, escribe el detalle en la salida estándar.
         */
        private void openConnection() {
            try {
                con = ConexionPoolDBCP.getConnection();
            } catch (SQLException e) {
                System.out.println("Error al intentar abrir la BD");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }  
        
        /**
         * Verifica si existe un usuario con las credenciales proporcionadas.
         *
         * @param username Nombre de usuario a comprobar
         * @param password Contraseña a comprobar
         * @return {@code true} si las credenciales son válidas; {@code false} en caso contrario
         */
        @Override
        public boolean checkUser(String username, String password) {
            boolean exists = false;
            this.openConnection();

            try {
                stmt = con.prepareStatement(SQLCHECK);
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                exists = rs.next(); // si hay al menos un resultado → existe

                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Error al comprobar usuario: " + e.getMessage());
            }

            return exists;
        }
        
        /**
         * Recupera un usuario por su nombre de usuario.
         *
         * @param username Nombre de usuario a buscar
         * @return instancia de {@link User} si existe; {@code null} si no se encuentra o si ocurre un error
         */
        @Override
        public User_ showUser(String username) {
            User_ u = null;
            this.openConnection();

            try {
                stmt = con.prepareStatement(SQLGETUSER);
                stmt.setString(1, username);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    u = new User_(
                        rs.getInt("USER_CODE"),
                        rs.getString("USER_NAME"),
                        rs.getString("PASSWD"),
                        rs.getString("EMAIL"),
                        rs.getString("NAME_"),
                        rs.getString("SURNAME"),
                        rs.getInt("TELEPHONE"),
                        rs.getInt("CARD_NO"),
                        rs.getString("GENDER")
                    );  
                }

                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println("Error al mostrar usuario: " + e.getMessage());
            }

            return u; // puede ser null si no existe
        }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 *
 * @author 2dami
 */
public class ConexionPoolDBCP {
     // Instancia única del pool
    private static BasicDataSource dataSource;
    // Configuración estática del pool
    static {
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/retocrud");
        dataSource.setUsername("root");
        dataSource.setPassword("abcd*1234");
        



// Parámetros del pool
        dataSource.setInitialSize(1);       // Conexiones iniciales
        dataSource.setMaxTotal(5);          // Máximo total de conexiones
        dataSource.setMinIdle(1);           // Mínimo de conexiones inactivas
        dataSource.setMaxIdle(5);           // Máximo de conexiones inactivas
        dataSource.setMaxWaitMillis(30000); // Tiempo máximo para esperar una conexión (30s)
    }


    // Método para obtener una conexión
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
   
}

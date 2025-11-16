/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author PIKAIN
 *
 *
 */
public class ConexionPoolDBCP {

    // Instancia única del pool
    private static BasicDataSource dataSource;

   

    // Configuración estática del pool
    static {
        dataSource = new BasicDataSource();
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/retocrud?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("abcd*1234");

        // Configuración del pool
        dataSource.setInitialSize(1);
        dataSource.setMaxTotal(3);       // max connections
        dataSource.setMaxIdle(1);        // max innactive connections
        dataSource.setMinIdle(1);        // min innactive connections

        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setMaxWaitMillis(5000);
        dataSource.setRemoveAbandonedOnBorrow(true);
        dataSource.setLogAbandoned(true);

        System.out.println(" Pool configurado con " + dataSource.getMaxTotal() + " conexiones máximas");
    }

    /*
     * Método para obtener una conexión con limitación utilizando la libreria Semaphore
     * 
     * @return una conexión de la base de datos
     */
    public static BasicDataSource getDataSource() {
        return dataSource;
    }
}

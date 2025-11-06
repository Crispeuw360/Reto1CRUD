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

/**
 *
 * @author 2dami
 */
public class ConexionPoolDBCP {
    // Instancia única del pool
    private static BasicDataSource dataSource;
    // Semáforo para limitar a 2 conexiones cada 30 segundos
    private static final Semaphore connectionSemaphore = new Semaphore(2, true);
    private static long lastResetTime = System.currentTimeMillis();
    private static final long RESET_INTERVAL = 30000; // 30 segundos
    
    // Configuración estática del pool
    static {
        dataSource = new BasicDataSource();
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/retocrud?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("abcd*1234");

        // Pool más pequeño para cumplir con la limitación
        dataSource.setInitialSize(1);
        dataSource.setMaxTotal(2); // MÁXIMO 2 conexiones
        dataSource.setMinIdle(1);
        dataSource.setMaxIdle(2);
        dataSource.setMaxWaitMillis(30000);

        // Configuraciones adicionales para evitar problemas
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(30000);
    }

    // Método para obtener una conexión con limitación
    public static Connection getConnection() throws SQLException {
        try {
            // Verificar si necesitamos resetear el semáforo (cada 30 segundos)
            synchronized (connectionSemaphore) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastResetTime > RESET_INTERVAL) {
                    // Resetear el semáforo cada 30 segundos
                    connectionSemaphore.drainPermits();
                    connectionSemaphore.release(2);
                    lastResetTime = currentTime;
                    System.out.println("🔄 Semáforo reseteado - Conexiones disponibles: 2");
                }
            }
            
            // Intentar adquirir un permiso (timeout de 30 segundos)
            if (connectionSemaphore.tryAcquire(30, TimeUnit.SECONDS)) {
                System.out.println("✅ Conexión adquirida. Conexiones restantes: " + connectionSemaphore.availablePermits());
                return dataSource.getConnection();
            } else {
                throw new SQLException("Timeout: No se pudo obtener conexión dentro de 30 segundos");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupción mientras se esperaba por conexión", e);
        }
    }
    
    // Método para liberar una conexión (debe llamarse cuando se cierra la conexión)
    public static void releaseConnection() {
        connectionSemaphore.release();
        System.out.println("🔓 Conexión liberada. Conexiones disponibles: " + connectionSemaphore.availablePermits());
    }

    // Método para verificar el estado del semáforo
    public static String getSemaphoreStatus() {
        return "Conexiones disponibles: " + connectionSemaphore.availablePermits() + 
               "/2 - Tiempo desde último reset: " + 
               (System.currentTimeMillis() - lastResetTime) + "ms";
    }
}
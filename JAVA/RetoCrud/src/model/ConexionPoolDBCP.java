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
 * Gestor avanzado de pool de conexiones a base de datos utilizando Apache DBCP2.
 * Implementa un sistema de limitación de conexiones concurrentes con semáforos
 * y reset automático periódico para prevenir bloqueos y optimizar el uso de recursos.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Límite configurable de conexiones concurrentes</li>
 *   <li>Reset automático cada 30 segundos para prevenir bloqueos</li>
 *   <li>Sistema de timeout para solicitudes de conexión</li>
 *   <li>Monitoreo del estado del pool en tiempo real</li>
 * </ul>
 * 
 * @author 2dami
 * @version 1.0
 */
public class ConexionPoolDBCP {
    // Instancia única del pool
    private static BasicDataSource dataSource;
    
    // VARIABLE CONFIGURABLE para el número de permisos
    private static final int MAX_CONCURRENT_CONNECTIONS = 6;
    
    // Semáforo configurable - CORREGIDO
    private static final Semaphore connectionSemaphore = new Semaphore(MAX_CONCURRENT_CONNECTIONS, true);
    private static final AtomicLong lastResetTime = new AtomicLong(System.currentTimeMillis());
    private static final long RESET_INTERVAL = 30000; // 30 segundos
    
    // Configuración estática del pool
    static {
        dataSource = new BasicDataSource();
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/retocrud?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("abcd*1234");

        // Pool configurado según el número máximo de conexiones
        dataSource.setInitialSize(1);
        dataSource.setMaxTotal(MAX_CONCURRENT_CONNECTIONS);
        dataSource.setMinIdle(1);
        dataSource.setMaxIdle(MAX_CONCURRENT_CONNECTIONS);
        dataSource.setMaxWaitMillis(10000); // 10 segundos

        // Configuraciones adicionales para evitar problemas
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        
        System.out.println("Pool configurado con " + MAX_CONCURRENT_CONNECTIONS + " conexiones máximas");
    }

    /**
     * Obtiene una conexión del pool con limitación de concurrencia.
     * Implementa un sistema de semáforos para controlar el número máximo
     * de conexiones concurrentes y un timeout de 10 segundos.
     * 
     * @return Conexión válida a la base de datos
     * @throws SQLException Si no se puede obtener conexión por timeout, 
     *         interrupción o error del pool
     */
    public static Connection getConnection() throws SQLException {
        try {
            // ✅ VERIFICAR Y RESETEAR PRIMERO
            checkAndResetSemaphore();
            
            int availableBefore = connectionSemaphore.availablePermits();
            System.out.println("Intentando conexión. Disponibles: " + availableBefore + "/" + MAX_CONCURRENT_CONNECTIONS + 
                             " - Tiempo desde reset: " + (System.currentTimeMillis() - lastResetTime.get()) + "ms");
            
            // Intentar adquirir un permiso (timeout de 10 segundos)
            if (connectionSemaphore.tryAcquire(10, TimeUnit.SECONDS)) {
                int availableAfter = connectionSemaphore.availablePermits();
                System.out.println("Conexión adquirida. Restantes: " + availableAfter + "/" + MAX_CONCURRENT_CONNECTIONS);
                
                // Obtener la conexión real del pool
                Connection conn = dataSource.getConnection();
                if (conn != null && !conn.isClosed()) {
                    return conn;
                } else {
                    // Si la conexión es inválida, liberar el permiso
                    connectionSemaphore.release();
                    throw new SQLException("Conexión nula o cerrada obtenida del pool");
                }
            } else {
                // ✅ VERIFICAR SI ES MOMENTO DE RESET
                checkAndResetSemaphore();
                System.out.println("TIMEOUT - Estado: " + getSemaphoreStatus());
                throw new SQLException("Timeout: No se pudo obtener conexión. " + getSemaphoreStatus());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupción mientras se esperaba por conexión", e);
        }
    }
    
    /**
     * Verifica y realiza un reset automático del semáforo cada 30 segundos.
     * Este método previene bloqueos permanentes liberando todos los permisos
     * y restableciendo el contador después del intervalo configurado.
     */
    private static void checkAndResetSemaphore() {
        long currentTime = System.currentTimeMillis();
        long lastReset = lastResetTime.get();
        
        if (currentTime - lastReset > RESET_INTERVAL) {
            // Solo un hilo puede resetear a la vez
            if (lastResetTime.compareAndSet(lastReset, currentTime)) {
                // ✅ RESET COMPLETO del semáforo
                int drained = connectionSemaphore.drainPermits();
                connectionSemaphore.release(MAX_CONCURRENT_CONNECTIONS);
                
                System.out.println("RESET AUTOMÁTICO - " + drained + " permisos drenados, " + 
                                 MAX_CONCURRENT_CONNECTIONS + " liberados. Tiempo: " + (currentTime - lastReset) + "ms");
            }
        }
    }
    
    /**
     * Libera una conexión y su permiso asociado en el semáforo.
     * Este método debe llamarse después de cerrar una conexión para
     * permitir que otras solicitudes puedan obtener conexiones.
     */
    public static void releaseConnection() {
        int availableBefore = connectionSemaphore.availablePermits();
        
        // ✅ VERIFICAR ANTES DE LIBERAR
        if (availableBefore < MAX_CONCURRENT_CONNECTIONS) {
            connectionSemaphore.release();
            System.out.println("Conexión liberada. Disponibles: " + (availableBefore + 1) + "/" + MAX_CONCURRENT_CONNECTIONS);
        } else {
            System.out.println("Intento de liberar conexión cuando ya hay máximo disponible: " + availableBefore + "/" + MAX_CONCURRENT_CONNECTIONS);
        }
        
        //VERIFICAR SI ES MOMENTO DE RESET después de liberar
        checkAndResetSemaphore();
    }

    /**
     * Obtiene el estado actual del semáforo y información de diagnóstico.
     * 
     * @return Cadena con información del estado del pool incluyendo
     *         conexiones disponibles y tiempo desde el último reset
     */
    public static String getSemaphoreStatus() {
        long timeSinceReset = System.currentTimeMillis() - lastResetTime.get();
        return "Conexiones disponibles: " + connectionSemaphore.availablePermits() + 
               "/" + MAX_CONCURRENT_CONNECTIONS + " - Tiempo desde último reset: " + 
               timeSinceReset + "ms" + (timeSinceReset > RESET_INTERVAL ? "(RESET PENDIENTE)" : "");
    }
    
    /**
     * Obtiene el número máximo configurado de conexiones concurrentes.
     * 
     * @return Número máximo de conexiones permitidas simultáneamente
     */
    public static int getMaxConnections() {
        return MAX_CONCURRENT_CONNECTIONS;
    }
    
    /**
     * Obtiene el número de conexiones disponibles en el pool.
     * 
     * @return Número de permisos disponibles en el semáforo
     */
    public static int getAvailableConnections() {
        return connectionSemaphore.availablePermits();
    }
    
    /**
     * Obtiene el tiempo transcurrido desde el último reset automático.
     * 
     * @return Tiempo en milisegundos desde el último reset
     */
    public static long getTimeSinceLastReset() {
        return System.currentTimeMillis() - lastResetTime.get();
    }
}
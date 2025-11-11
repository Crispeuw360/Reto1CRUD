package model;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.scene.control.Alert;

/**
 * Hilo especializado para gestionar el tiempo de vida de una conexión a base de datos.
 * Esta clase extiende Thread y se encarga de mantener una conexión activa durante
 * un tiempo determinado (30 segundos) antes de cerrarla automáticamente, previniendo
 * así conexiones huérfanas y optimizando el uso del pool de conexiones.
 * 
 * @author Estudiante DAM
 * @version 1.0
 */
public class ThreadConexion extends Thread {
    private Connection conexion;
    private boolean connected;

    /**
     * Constructor que inicializa el hilo con una conexión a base de datos.
     * 
     * @param conexion La conexión a base de datos que será gestionada por este hilo
     */
    public ThreadConexion(Connection conexion) {
        this.conexion = conexion;
    }
    
    /**
     * Método principal de ejecución del hilo.
     * Mantiene la conexión activa durante 30 segundos y luego la cierra
     * automáticamente. Maneja interrupciones mostrando alertas al usuario
     * y garantiza que la conexión se cierre incluso en caso de error.
     */
    @Override
    public void run() {
        try {
            // Marcar la conexión como activa
            connected = true;
            System.out.println("Conexión establecida");
            
            // Mantener la conexión activa durante 30 segundos
            Thread.sleep(30000);
            
            // Marcar la conexión como liberada
            connected = false;
            System.out.println("Conexión liberada");
            
        } catch (InterruptedException e) {
            // Manejar interrupción del hilo mostrando una alerta al usuario
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al liberar la conexión");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo liberar la conexión.");
            alert.showAndWait();
        } finally {
            // Bloque finally que garantiza el cierre de la conexión
            try {
                if (conexion != null) {
                    conexion.close();
                    System.out.println("Conexión cerrada");
                }
            } catch (SQLException e) {
                // Manejar errores de cierre de conexión
                e.printStackTrace();
            }
        }
    }
}
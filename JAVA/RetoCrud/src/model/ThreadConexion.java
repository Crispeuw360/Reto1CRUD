package model;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.scene.control.Alert;

/**
 * Clase que representa un hilo de ejecución para liberar una conexión. Hereda
 * de la clase {@link Thread} y se encarga de liberar una conexión después de un
 * cierto tiempo de espera.
 *
 * @author pikain
 */
public class ThreadConexion extends Thread {

    private Connection conexion;
    private ConexionPoolStack pool;

    /**
     * Constructor de la clase ThreadConexion.
     *
     * @param conexion Conexión a liberar.
     */
    public ThreadConexion(Connection conexion, ConexionPoolStack pool) {
        this.conexion = conexion;
        this.pool = pool;
    }

    /**
     * Método que se ejecuta cuando se inicia el hilo.
     *
     * @throws InterruptedException Si se interrumpe el hilo.
     */
    @Override
    public void run() {
        try {
            System.out.println("Conexión establecida");
            Thread.sleep(30000);
            System.out.println("Conexión liberada");
        } catch (InterruptedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al liberar la conexión");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo liberar la conexión.");
            alert.showAndWait();
        } finally {

            if (conexion != null && pool != null) {
                pool.releaseConnection(conexion);
                System.out.println("Conexión cerrada");
            }

        }
    }
}

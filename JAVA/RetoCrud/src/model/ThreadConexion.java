package model;

import java.sql.Connection;
import java.sql.SQLException;

public class ThreadConexion extends Thread {
    private Connection conexion;
    private boolean connected;

    public ThreadConexion(Connection conexion) {
        this.conexion = conexion;
    }
    
    @Override
    public void run() {
        try {
            connected = true;
            System.out.println("Conexión establecida");
            Thread.sleep(30000);
            connected = false;
            System.out.println("Conexión liberada");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                if (conexion != null) {
                    conexion.close();
                    System.out.println("Conexión cerrada");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Exception.ErrorException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConexionPoolStack {

    private static String url;
    private static String username;
    private static String password;
    private static int maxPoolSize = 2;
    private static Stack<Connection> availableConnections = new Stack<>();
    private static int totalConnections = 0;

    static {
        try {
            url = "jdbc:mysql://localhost:3306/retocrud?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true";
            username = "root";
            password = "abcd*1234";
            
            for (int i = 0; i < maxPoolSize; i++) {
                Connection con = DriverManager.getConnection(url, username, password);
                availableConnections.push(con);
                totalConnections++;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error configurating connections pool", e);
        }
    }

    public synchronized Connection getConnection() throws ErrorException {
        // 1. Usar conexiones disponibles primero
        if (!availableConnections.isEmpty()) {
            Connection con = availableConnections.pop();
            System.out.println("🔵 Conexión obtenida del pool. Disponibles: " + availableConnections.size());
            return con;
        }
        
        if (totalConnections < maxPoolSize) {
            try{
                Connection con = DriverManager.getConnection(url, username, password);
                totalConnections++;
                return con;
                        
            }catch(SQLException e){
                throw new ErrorException("error en el sql ","error en el sql ");
            }
        }
        
        try {
            System.out.println("⏳ No hay conexiones disponibles. Esperando...");
            this.wait();
            // Cuando se notifica, intentar de nuevo
            return getConnection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ErrorException("Interrumpido esperando conexión", e.getMessage());
        }
        
    }

    public synchronized void releaseConnection(Connection conn) {
        availableConnections.push(conn);
        this.notifyAll();
    }

    public synchronized void closeAll() throws SQLException {
        while (!availableConnections.isEmpty()) {
            Connection conn = availableConnections.pop();
            conn.close();
        }
        totalConnections = 0;
    }
}

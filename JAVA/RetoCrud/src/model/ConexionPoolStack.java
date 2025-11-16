/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConexionPoolStack {

    private final String url;
    private final String username;
    private final String password;
    private final int maxPoolSize;
    private final Stack<Connection> availableConnections = new Stack<>();
    private int totalConnections = 0;

    public ConexionPoolStack(String url, String username, String password, int maxPoolSize) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxPoolSize = maxPoolSize;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (!availableConnections.isEmpty()) {
            return availableConnections.pop();
        }
        if (totalConnections < maxPoolSize) {
            Connection conn = DriverManager.getConnection(url, username, password);
            totalConnections++;
            return conn;
        }
        try {
            this.wait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrumpido mientras se espera conexión", e);
        }
        return getConnection();
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

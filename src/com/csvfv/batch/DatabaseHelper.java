package com.csvfv.batch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static DatabaseHelper instance;
    private Connection connection;
    private String url = "jdbc:postgresql://localhost:5432/";
    private String username = "postgres";
    private String password = "Nikel123";

    private DatabaseHelper() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url + "nikel", username, password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseHelper getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseHelper();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseHelper();
        }

        return instance;
    }
}

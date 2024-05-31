package com.example.chatsystem.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ChatDatabase {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "chat_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Ga526424...?";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        createDatabase();
        createTables();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL + DB_NAME, USERNAME, PASSWORD);
    }

    public static void createDatabase() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            statement.executeUpdate(sql);
            System.out.println("Database created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTables() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS peers ("
                    + "peer_id VARCHAR(255) PRIMARY KEY,"
                    + "peer_name VARCHAR(255)"
                    + "ip VARCHAR(255),"
                    + "port INT"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Table 'peers' created successfully");

            sql = "CREATE TABLE IF NOT EXISTS messages ("
                    + "id VARCHAR(255) PRIMARY KEY,"
                    + "timestamp BIGINT,"
                    + "content VARCHAR(255),"
                    + "peer_id VARCHAR(255),"
                    + "FOREIGN KEY (peer_id) REFERENCES peers(peer_id)"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Table 'messages' created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

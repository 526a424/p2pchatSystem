package com.example.chatsystem.service;

import com.example.chatsystem.model.Peer;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatService extends HttpServlet {

    private List<Peer> peers;
    private Connection connection;

    public ChatService() {
        // 初始化 peers 列表
        this.peers = new ArrayList<>();
    }

    // 连接到数据库
    public void connectToDatabase() throws SQLException {
        // 假设你已经正确配置了数据库连接信息
        String url = "jdbc:mysql://localhost:3306/chat_system";
        String username = "root";
        String password = "Ga526424...?";
        connection = DriverManager.getConnection(url, username, password);
    }

    // 关闭数据库连接
    public void disconnectFromDatabase() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    // 启动服务端
    public void startServer() {
        try {
            connectToDatabase();
            // 执行其他初始化操作
            System.out.println("Server started.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }

    // 关闭服务端
    public void stopServer() {
        try {
            disconnectFromDatabase();
            // 执行其他清理操作
            System.out.println("Server stopped.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to stop server: " + e.getMessage());
        }
    }

}

package com.example.chatsystem.Dao;

import com.example.chatsystem.Utils.DBUtils;
import com.example.chatsystem.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {

    @Override
    public int insertMessage(Message message) {
        Connection connection = DBUtils.getConnection();
        int rowsAffected = 0;
        String sql = "INSERT INTO messages (id, timestamp, content, peer_id) VALUES (?, ?, ?, ?);";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, message.getId());
            statement.setLong(2, message.getTimestamp());
            statement.setString(3, message.getContent());
            statement.setString(4, message.getPeerId());
            rowsAffected = statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.closeConnection(connection);
        }
        return rowsAffected > 0 ? 1 : 0; // 如果影响行数大于0，返回1，否则返回0
    }

    @Override
    public List<Message> getMessagesByPeerId(String peerId) {
        Connection connection = DBUtils.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE peer_id = ?;";
        try  {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, peerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                long timestamp = resultSet.getLong("timestamp");
                String content = resultSet.getString("content");
                Message message = new Message(id, timestamp, content, peerId);
                messages.add(message);
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(connection);
        }
        return messages;
    }

    @Override
    public int deleteMessagesByPeerId(String peerId) {
        Connection connection = DBUtils.getConnection();
        int rowsAffected = 0;
        String sql = "DELETE FROM messages WHERE peer_id = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, peerId);
            rowsAffected = statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(connection);
        }
        return rowsAffected > 0 ? 1 : 0;
    }

    @Override
    public List<Message> getAllMessages() {
        Connection connection = DBUtils.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages ORDER BY timestamp;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                long timestamp = resultSet.getLong("timestamp");
                String content = resultSet.getString("content");
                String peerId = resultSet.getString("peer_id");
                Message message = new Message(id, timestamp, content, peerId);
                messages.add(message);
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(connection);
        }
        return messages;
    }

}

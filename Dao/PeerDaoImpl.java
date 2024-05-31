package com.example.chatsystem.Dao;

import com.example.chatsystem.Utils.DBUtils;
import com.example.chatsystem.model.Peer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PeerDaoImpl implements PeerDao {

    @Override
    public int insertPeer(Peer peer) {
        Connection connection = DBUtils.getConnection();
        String sql = "INSERT INTO peers (peer_id, peer_name, ip, port) VALUES (?, ?, ?, ?);";
        int rowsAffected = 0;
        try  {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, peer.getPeerId());
            statement.setString(2, peer.getPeerName());
            statement.setString(3, peer.getIp());
            statement.setInt(4, peer.getPort());
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
    public int deletePeer(String peerId) {
        Connection connection = DBUtils.getConnection();
        String sql = "DELETE FROM peers WHERE peer_id = ?;";
        int rowsAffected = 0;
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

    public int getPeerCount() {
        Connection connection = DBUtils.getConnection();
        int count = 0;
        String sql = "SELECT COUNT(*) FROM peers;";
        try  {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(connection);
        }
        return count;
    }

    @Override
    public Peer findByName(String name) {
        Connection connection = DBUtils.getConnection();
        Peer peer = null;
        String sql = "SELECT * FROM peers WHERE peer_name = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                peer = new Peer();
                peer.setPeerId(resultSet.getString("peer_id"));
                peer.setPeerName(resultSet.getString("peer_name"));
                peer.setIp(resultSet.getString("ip"));
                peer.setPort(resultSet.getInt("port"));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(connection);
        }
        return peer;
    }

    @Override
    public Peer findById(int id) {
        Connection connection = DBUtils.getConnection();
        Peer peer = null;
        String sql = "SELECT * FROM peers WHERE peer_id = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                peer = new Peer();
                peer.setPeerId(resultSet.getString("peer_id"));
                peer.setPeerName(resultSet.getString("peer_name"));
                peer.setIp(resultSet.getString("ip"));
                peer.setPort(resultSet.getInt("port"));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(connection);
        }
        return peer;
    }
}

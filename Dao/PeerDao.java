package com.example.chatsystem.Dao;

import com.example.chatsystem.model.Peer;

public interface PeerDao {
    int insertPeer(Peer peer);
    int deletePeer(String peerId);
    int getPeerCount();
    Peer findByName(String name);
    Peer findById(int id);
}


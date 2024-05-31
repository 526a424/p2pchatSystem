package com.example.chatsystem.Dao;

import com.example.chatsystem.model.Message;

import java.util.List;

public interface MessageDao {
    int insertMessage(Message message);//成功返回1 失败0
    List<Message> getMessagesByPeerId(String peerId);
    int deleteMessagesByPeerId(String peerId); // 成功返回1 失败返回0
    List<Message> getAllMessages(); // 获取所有聊天信息
}
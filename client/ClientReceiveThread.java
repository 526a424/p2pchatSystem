package com.example.chatsystem.client;

import com.example.chatsystem.Dao.PeerDao;
import com.example.chatsystem.model.Peer;

import java.net.*;
import java.io.*;
import java.awt.*;

public class ClientReceiveThread extends Thread {
    Socket socket;
    List list;
    TextArea taRecord;
    TextField textfield;
    ObjectInputStream in;
    ObjectOutputStream out;
    Peer peer;
    PeerDao peerDao;
    InetAddress ip;
    int port;
    SocketAddress ip1;
    ServerSocket serversocket;
    int selectedPort;
    TextArea taInput;

    public ClientReceiveThread(Peer peer, PeerDao peerDao, Socket socket, ObjectInputStream in,
                               ObjectOutputStream out, List list, TextArea taRecord,
                               TextArea taInput, TextField textfield, InetAddress ip, int port,
                               int selectedPort) {
        this.peer = peer;
        this.peerDao = peerDao;
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.list = list;
        this.taRecord = taRecord;
        this.taInput = taInput;
        this.textfield = textfield;
        this.ip = ip;
        this.port = port;
        this.selectedPort = selectedPort;
        // this.ip1=ip1;

    }

    public void run() {

        try {
            serversocket = new ServerSocket(this.port);
            //taRecord.append("端口号为" + port + "的客户正在监听" + "\n");

            while (true) {
                Socket clientsocket = serversocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientsocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientsocket
                        .getInputStream());
                String type = (String) in.readObject();
                if (type.equalsIgnoreCase("聊天信息")) {
                    String name=(String)in.readObject();
                    String mess = (String) in.readObject();
                    taRecord.append(name+"说:" + "\n");
                    taRecord.append(" "+mess+"\n");
                }
                out.close();
                in.close();
                clientsocket.close();
            }

        } catch (Exception e1) {
            taRecord.append("error47" + e1.toString());
        }
    }

}

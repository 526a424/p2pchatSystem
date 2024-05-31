package com.example.chatsystem.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSendThread extends Thread {
    private InetAddress remoteAddress = null;
    private int remotePort = 0;
    private String message = null;
    private String name=null;
    public ClientSendThread(InetAddress address, int port, String message,String name){
        this.remoteAddress = address;
        this.remotePort = port;
        this.message = message;
        this.name=name;
    }

    public void run(){
        try {
            Socket socket = new Socket(this.remoteAddress, this.remotePort);
            ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
            out.writeObject("聊天信息");
            out.writeObject(name);
            out.writeObject(this.message);
            out.flush();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

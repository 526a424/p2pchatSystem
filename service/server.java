package com.example.chatsystem.service;

import com.example.chatsystem.Dao.MessageDao;
import com.example.chatsystem.Dao.MessageDaoImpl;
import com.example.chatsystem.Dao.PeerDao;
import com.example.chatsystem.Dao.PeerDaoImpl;
import com.example.chatsystem.model.Message;
import com.example.chatsystem.model.Peer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class server extends JFrame implements ActionListener {
    JButton sendButton, cleanButton, closeButton, startButton;
    TextArea taRecord, taInput;//创建多行文本输入框 ->显示记录和输入文本
    TextField textfield;//创建单行文本输入框
    List list;
    Peer peer;
    PeerDao peerDao;
    Message message;
    MessageDao messageDao;
    ServerSocket serverSocket;
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    ArrayList<ObjectOutputStream> allOut;
    static String ip;
    static int port;
    static boolean isStop;
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd hh:mm EEEE");
    Date time = new Date();
    static server server;

    public server() {
        //初始化allOuts
        this.allOut = new ArrayList<ObjectOutputStream>();
        sendButton = new JButton("发送");
        sendButton.setMnemonic('S');
        cleanButton = new JButton("清除");
        closeButton = new JButton("关闭");
        startButton = new JButton("启动");
        closeButton.setEnabled(false);
        sendButton.setEnabled(false);
        cleanButton.setEnabled(false);

        taRecord = new TextArea("", 14, 50);
        taRecord.setBackground(Color.lightGray);
        taInput = new TextArea("", 4, 50);
        taInput.setBackground(Color.lightGray);
        textfield = new TextField();
        textfield.setBackground(Color.lightGray);
        taRecord.setEditable(false);
        //list.setEnabled(false);
        textfield.setEditable(false);

        //DefaultListModel model=new DefaultListModel();
        list = new List();

        Panel p1 = new Panel();
        p1.setLayout(new BorderLayout());
        p1.add(new Label("在线列表"), BorderLayout.NORTH);
        p1.add(textfield, BorderLayout.CENTER);

        Panel p2 = new Panel();
        p2.setLayout(new BorderLayout());
        p2.add(p1, BorderLayout.NORTH);
        p2.add(list, BorderLayout.CENTER);

        Panel p3 = new Panel();
        p3.setLayout(new GridLayout(1, 2, 90, 4));
        p3.add(startButton);
        p3.add(closeButton);

        Panel p4 = new Panel();
        p4.setLayout(new BorderLayout());
        p4.add(new Label("聊天记录"), BorderLayout.NORTH);
        p4.add(taRecord, BorderLayout.CENTER);

        Panel p9 = new Panel();
        p9.setLayout(new BorderLayout());
        p9.add(p3, BorderLayout.NORTH);
        p9.add(p4, BorderLayout.CENTER);

        Panel p5 = new Panel();
        p5.setLayout(new BorderLayout(5, 9));
        p5.add(p9, BorderLayout.CENTER);
        p5.add(taInput, BorderLayout.SOUTH);

        Panel p6 = new Panel();
        p6.setLayout(new GridLayout(1, 2, 90, 50));
        p6.add(sendButton);
        p6.add(cleanButton);

        Panel p7 = new Panel();
        p7.setLayout(new BorderLayout());
        p7.add(p5, BorderLayout.CENTER);
        p7.add(p6, BorderLayout.SOUTH);

        Panel p8 = new Panel();
        p8.setLayout(new BorderLayout());
        p8.add(p2, BorderLayout.WEST);
        p8.add(p7, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(p8, BorderLayout.CENTER);

        setSize(450, 450);
        setTitle("chatRoom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        sendButton.addActionListener(this);
        cleanButton.addActionListener(this);
        closeButton.addActionListener(this);
        startButton.addActionListener(this);
    }

    public static void main(String[] args) {
        server = new server();
    }

    public void startServer() {

        try {
            serverSocket = new ServerSocket(3000);
            taRecord.append("等待连线....");
            startButton.setEnabled(false);
            closeButton.setEnabled(true);
            sendButton.setEnabled(true);
            cleanButton.setEnabled(true);
            peer = new Peer();
            peerDao = new PeerDaoImpl();
            message = new Message();
            messageDao = new MessageDaoImpl();
            ServerListenThread serverListenThread = new ServerListenThread(serverSocket, taRecord, textfield, list, peer, peerDao, message, messageDao);
            serverListenThread.start();
        } catch (IOException e) {
            taRecord.append(e.toString());
        }
    }

    public void stopServer() {
        try {
            this.isStop = true;
            serverSocket.close();
            socket.close();
            list.removeAll();
        } catch (Exception e) {
            taRecord.append("close");
        }
    }

    public void sendSystemMessage() {
        String message = taInput.getText();
        taRecord.append("系统消息  " + taInput.getText() + "\n");
        taInput.setText("");

        try {
            Iterator<ObjectOutputStream> it = this.allOut.iterator();
            while (it.hasNext()) {
                ObjectOutputStream tout = it.next();
                tout.writeObject("系统消息");
                tout.flush();
                tout.writeObject(message);
                tout.flush();
            }

        } catch (Exception e) {
            taRecord.append("error92" + e.toString());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startButton)
        {
            startServer();
        }
        else if(e.getSource()==closeButton)
        {
            stopServer();
            System.exit(0);
        }
        else if(e.getSource()==sendButton)
        {
            if(taInput.getText().equalsIgnoreCase("")||taInput.getText()==null)
            {
                JOptionPane.showMessageDialog(this, "尚未输入系统信息!","Information" , JOptionPane.INFORMATION_MESSAGE);
            }
            sendSystemMessage();
        }
        else if(e.getSource()==cleanButton)
        {
            taInput.setText("");
        }
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }


    public class ServerListenThread extends Thread {
        ServerSocket serversocket;
        TextArea taRecord;
        List list;
        TextField textfield;
        Peer peer;
        PeerDao peerDao;
        Message message;
        MessageDao messageDao;
        Socket socket;
        ObjectOutputStream out;
        ObjectInputStream in;
        ServerReceiveThread serverreceivethread;

        //serverSocket,taRecord,textfield,list,peer,peerDao,message,messageDao
        public ServerListenThread(ServerSocket serversocket, TextArea taRecord, TextField textfield, List list,
                                  Peer peer, PeerDao peerDao, Message message, MessageDao messageDao) {
            this.serversocket = serversocket;
            this.taRecord = taRecord;
            this.textfield = textfield;
            this.list = list;
            this.peer = new Peer();
            this.peerDao = new PeerDaoImpl();
            this.message = new Message();
            this.messageDao = new MessageDaoImpl();

        }

        public void run() {
            while (!isStop && !serversocket.isClosed()) {
                try {
                    Peer peer = new Peer();
                    socket = serversocket.accept();
                    String ip = String.valueOf(socket.getInetAddress().getByName(socket.getInetAddress().getHostAddress()));
                    server.setIp(ip);
                    peer.setIp(ip);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    allOut.add(out);
                    in = new ObjectInputStream(socket.getInputStream());
                    peer.setPeerName((String) in.readObject());
                    peer.setPort(in.readInt());
                    taRecord.append("恭喜你!" + peer.getPeerName() + "连线成功!" + " " + "the client " + peer.getPeerName() + "'s address is " + ip + ":" + peer.getIp() + "\n");

                    list.add(peer.getPeerName());
                    peerDao.insertPeer(peer);
                    taRecord.append("用户" + peer.getPeerName() + "已上线\n");
                    String mes = "在线用户" + peerDao.getPeerCount() + "人\n";
                    textfield.setText(mes);

                    server.setOut(out);

                    serverreceivethread = new ServerReceiveThread(socket, taRecord, textfield, list, peer, peerDao, message, messageDao, in, out, server.this.allOut, mes);
                    serverreceivethread.start();
                } catch (Exception e) {
                    taRecord.append("error85" + e.toString());
                }
            }
        }
    }
}
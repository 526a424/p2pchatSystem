package com.example.chatsystem.service;

import com.example.chatsystem.Dao.MessageDao;
import com.example.chatsystem.Dao.MessageDaoImpl;
import com.example.chatsystem.Dao.PeerDao;
import com.example.chatsystem.Dao.PeerDaoImpl;
import com.example.chatsystem.model.Message;
import com.example.chatsystem.model.Peer;

import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerReceiveThread extends Thread{
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
    ArrayList<ObjectOutputStream> allOut;
    boolean isStop;
    String mes;

    public ServerReceiveThread(Socket socket,TextArea taRecord,TextField textfield,List list,
                               Peer peer, PeerDao peerDao, Message message, MessageDao messageDao,
                               ObjectInputStream in,ObjectOutputStream out,ArrayList<ObjectOutputStream> allOut,String mes)
    {
        this.socket=socket;
        this.taRecord=taRecord;
        this.textfield=textfield;
        this.list=list;
        this.peer = new Peer();
        this.peerDao = new PeerDaoImpl();
        this.message = new Message();
        this.messageDao = new MessageDaoImpl();
        this.in=in;
        this.out=out;
        this.isStop=false;
        this.allOut = allOut;
        this.mes=mes;
    }
    public void run()
    {
        sendUserList();//!isStop&&!socket.isClosed()
        while(true)
        {
            try
            {
                String request=(String)in.readObject();
                if(request.equalsIgnoreCase("用户下线"))
                {
                    Peer client=peerDao.findByName(peer.getPeerName());
                    int j=searchIndex(peerDao,peer.getPeerName());
                    //taRecord.append("所删除用户的序号为:"+j+"\n");
                    peerDao.deletePeer(client.getPeerName());;
                    this.allOut.remove(j);
                    String msg="用户"+peer.getPeerName()+"下线\n";
                    list.removeAll();
                    int count=peerDao.getPeerCount();
                    taRecord.append(msg);
                    taRecord.append("用户下线后的人数"+count+"\n");
                    int i=0;
                    while(i<count)
                    {
                        client=peerDao.findById(i);
                        if(client==null)
                        {
                            i++;
                            continue;
                        }
                        list.add(client.getPeerName());
                        i++;
                    }
                    this.mes="在线用户"+peerDao.getPeerCount()+"人\n";
                    textfield.setText(this.mes);
                    sendUserList();
                    sendToAll(msg);
                    break;
                }
                //out.close();
                //in.close();
                //socket.close();

            }catch(Exception e)
            {
                taRecord.append("error1"+e.toString()+"\n");
            }
        }
    }

    public void sendToAll(String msg)
    {
        try
        {
            Iterator<ObjectOutputStream> it = this.allOut.iterator();
            while(it.hasNext())
            {
                ObjectOutputStream tout = it.next();
                tout.writeObject("下线信息");
                tout.flush();
                tout.writeObject(msg);
                tout.flush();
                //socket.close();
            }

        }
        catch(Exception e)
        {
            taRecord.append("error2"+e.toString());
        }
    }
    public void sendUserList()
    {
        String userlist="";
        int count=peerDao.getPeerCount();
        int i=0;
        while(i<count)
        {
            Peer client = peerDao.findById(i);
            if(client==null)
            {
                i++;
                continue;
            }
            userlist+=client.getPeerName();
            userlist+="@@";
            i++;
        }

        try
        {
            Iterator<ObjectOutputStream> it = this.allOut.iterator();
            while(it.hasNext()){
                ObjectOutputStream tout = it.next();
                tout.writeObject("用户列表");
                tout.flush();
                tout.writeObject(userlist);
                tout.flush();
                tout.writeObject(this.message);
                tout.flush();
                //tout.writeObject(clientinfo);
                //tout.flush();
                //socket.close();
                if(tout!=out)
                {
                    tout.writeObject(peerDao.findById(peerDao.getPeerCount()-1));
                    tout.flush();
                }
                else
                {
                    tout.writeObject(peerDao);
                    tout.flush();
                }
            }

            if(peerDao.getPeerCount()!=0)
            {
                taRecord.append("当前在线用户有:");
                for(int r=0;r<peerDao.getPeerCount();r++)
                {
                    taRecord.append(" "+peerDao.findById(i).getPeerName());
                }
                taRecord.append(" "+"\n");
            }
        }
        catch(Exception e)
        {
            taRecord.append("error09"+e.toString()+"\n");
        }
    }
    public int searchIndex(PeerDao peerDao,String name)
    {
        int count=peerDao.getPeerCount();
        int i=0;
        while(i<count)
        {
            Peer client=peerDao.findById(i);
            if(!name.equalsIgnoreCase(client.getPeerName()))
                i++;
            else
                return i;
        }
        return i;
    }
}

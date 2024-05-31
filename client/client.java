package com.example.chatsystem.client;

import com.example.chatsystem.Dao.MessageDao;
import com.example.chatsystem.Dao.PeerDao;
import com.example.chatsystem.Dao.PeerDaoImpl;
import com.example.chatsystem.Utils.portUtil;
import com.example.chatsystem.model.Message;
import com.example.chatsystem.model.Peer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
public class client extends JFrame implements ActionListener{
    static final long serialVersionUID = 42L;//这是什么呀???????????????????????
    JButton sendButton,cleanButton,logoutButton,loginButton;
    TextArea taRecord,taInput;
    TextField textfield,usernametext;
    List list;
    ObjectOutputStream out;
    ObjectInputStream in;
    Socket socket;
    Peer peer;
    PeerDao peerDao;
    Message message;
    MessageDao messageDao;
    int selectedPort;
    int clientListenPort ;
    InetAddress ip;
    int port;
    ClientReceiveThread clientreceivethread;
    String username;
    static client client;
    SocketAddress socketAddress;
    SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd hh:mm EEEE");
    Date time=new Date();
    DefaultListModel model=new DefaultListModel();
    public static void main(String[] args)
    {
        client=new client();
    }
    public client()
    {

        sendButton=new JButton("发送");
        sendButton.setMnemonic('S');
        cleanButton=new JButton("清除");
        logoutButton=new JButton("关闭");
        loginButton=new JButton("登录");
        logoutButton.setEnabled(false);
        sendButton.setEnabled(false);
        cleanButton.setEnabled(false);

        taRecord=new TextArea("",14,50);
        taRecord.setBackground(Color.lightGray);
        taInput=new TextArea("",4,50);
        taInput.setBackground(Color.lightGray);
        textfield=new TextField();
        textfield.setBackground(Color.lightGray);
        usernametext=new TextField();
        usernametext.setBackground(Color.lightGray);
        taRecord.setEditable(false);
        textfield.setEditable(false);

        list=new List();
        list.add("all");

        Panel p1=new Panel();
        p1.setLayout(new BorderLayout());
        p1.add(new Label("在线列表"),BorderLayout.NORTH);
        p1.add(textfield,BorderLayout.CENTER);

        Panel p2=new Panel();
        p2.setLayout(new BorderLayout());
        p2.add(p1,BorderLayout.NORTH);
        p2.add(list,BorderLayout.CENTER);

        Panel p3=new Panel();
        p3.setLayout(new GridLayout(1,4));
        p3.add(new Label("用户名"));
        p3.add(usernametext);
        p3.add(loginButton);
        p3.add(logoutButton);

        Panel p4=new Panel();
        p4.setLayout(new BorderLayout());
        p4.add(new Label("聊天记录"),BorderLayout.NORTH);
        p4.add(taRecord,BorderLayout.CENTER);

        Panel p9=new Panel();
        p9.setLayout(new BorderLayout());
        p9.add(p3,BorderLayout.NORTH);
        p9.add(p4,BorderLayout.CENTER);

        Panel p5=new Panel();
        p5.setLayout(new BorderLayout(5,9));
        p5.add(p9,BorderLayout.CENTER);
        p5.add(taInput,BorderLayout.SOUTH);

        Panel p6=new Panel();
        p6.setLayout(new GridLayout(1,2,90,50));
        p6.add(sendButton);
        p6.add(cleanButton);

        Panel p7=new Panel();
        p7.setLayout(new BorderLayout());
        p7.add(p5,BorderLayout.CENTER);
        p7.add(p6,BorderLayout.SOUTH);

        Panel p8=new Panel();
        p8.setLayout(new BorderLayout(5,5));
        p8.add(p2,BorderLayout.WEST);
        p8.add(p7,BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(p8,BorderLayout.CENTER);

        setSize(450,450);
        setTitle("chatRoom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        sendButton.addActionListener(this);
        cleanButton.addActionListener(this);
        loginButton.addActionListener(this);
        logoutButton.addActionListener(this);
        list.addActionListener(this);
        //list.addMouseListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==loginButton)
        {
            Login();
        }
        else if(e.getSource()==logoutButton)
        {
            Logout();
            System.exit(0);
        }
        else if(e.getSource()==sendButton)
        {
            if(taInput.getText().equalsIgnoreCase("")||taInput.getText()==null)
            {
                JOptionPane.showMessageDialog(this, "您尚未输入聊天信息!","Information" , JOptionPane.INFORMATION_MESSAGE);
            }
            sendMessage();
        }
        else if(e.getSource()==cleanButton)
        {
            taInput.setText("");
        }
    }

    public void Login()
    {
        new Thread(new ComWithServer()).start();
    }
    public void setUsername(String username)
    {
        this.username=usernametext.getText();
    }
    public String getUsername()
    {
        return username;

    }
    public void setOut(ObjectOutputStream out)
    {
        this.out=out;
    }
    public ObjectOutputStream getOut()
    {
        return out;
    }

    public void Logout()
    {
        logoutButton.setEnabled(false);
        sendButton.setEnabled(false);
        cleanButton.setEnabled(false);
        loginButton.setEnabled(true);

        if(socket.isClosed())
        {
            return;
        }
        try
        {
            ObjectOutputStream out=client.getOut();
            out.writeObject("用户下线");
            out.flush();
            in.close();
            out.close();
            //socket.close();
        }
        catch(Exception e)
        {
            taRecord.append("error92"+e.toString());
        }
    }
    public void sendMessage()
    {
        try
        {
            if(list.getSelectedIndex()==0)//群发
            {
                String name=usernametext.getText();
                String msgg=taInput.getText();
                //taRecord.append(usernametext.getText()+"说: "+taInput.getText());
                for(int j=0;j<peerDao.getPeerCount();j++)
                {
                    String ip1 = peerDao.findById(j).getIp();
                    int port1= peerDao.findById(j).getPort();
                    InetAddress inetAddress = InetAddress.getByName(ip1);
                    new ClientSendThread(inetAddress,port1,msgg,name).start();
                }
                taInput.setText("");

            }
            else  //给指定用户发送信息
            {
                String toSomebody=list.getSelectedItem().toString();
                taRecord.append(usernametext.getText()+"对"+toSomebody+"说:"+"\n");
                taRecord.append(" "+taInput.getText()+"\n");
                String name=usernametext.getText();
                String msg=taInput.getText();
                //clientInfo clientinfo=client.getClientInfo();
                //taRecord.append("用户数为:"+clientinfo.getCount()+"\n");
                String ip2 = peerDao.findByName(toSomebody).getIp();
                InetAddress inetAddress = InetAddress.getByName(ip2);
                int port = peerDao.findByName(toSomebody).getPort();
                //taRecord.append(toSomebody+"的IP是:"+ip+":"+port+"\n");
                client.setSelectedPort(port);
                String ip1=inetAddress.getHostAddress();

                Socket clientsocket=new Socket(ip1,port);

                ObjectOutputStream out=new ObjectOutputStream(clientsocket.getOutputStream());
                out.writeObject("聊天信息");
                out.flush();
                out.writeObject(name);
                out.flush();
                out.writeObject(msg);
                out.flush();
                out.close();
                clientsocket.close();
                //new ClientSendThread(ip, port, msg,name).start();
                taInput.setText("");
            }

        }
        catch(Exception ec)
        {
            taRecord.append("和其他在线用户聊天时出现错误!"+ec.toString());
        }
    }

    public class ComWithServer implements Runnable{


        public void run() {

            try
            {
                peer = new Peer();
                peerDao = new PeerDaoImpl();
                socket=new Socket("127.0.0.1",1234);
                ip=socket.getLocalAddress();
                client.setIp(ip);
                client.setPort(client.this.clientListenPort);
                taRecord.append("恭喜你!"+usernametext.getText()+"你已连线成功,你的IP和端口号为:"+ip+"\n");
                clientListenPort= portUtil.getAvailablePort();
                out=new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(usernametext.getText());
                out.flush();
                out.writeInt(client.this.clientListenPort);
                out.flush();
                client.setOut(out);
                client.setUsername(usernametext.getText());
                in=new ObjectInputStream(socket.getInputStream());

                int selectedPort=client.getSelectedPort();
                clientreceivethread=new ClientReceiveThread(peer,peerDao,socket,in,out,list,taRecord,taInput,textfield,ip,client.this.clientListenPort,selectedPort);
                clientreceivethread.start();
                loginButton.setEnabled(false);
                logoutButton.setEnabled(true);
                sendButton.setEnabled(true);
                cleanButton.setEnabled(true);
                while(true)
                {
                    try
                    {
                        String type=(String)in.readObject();
                        if(type.equalsIgnoreCase("用户列表"))
                        {
                            String userlist=(String)in.readObject();
                            String username[]=userlist.split("@@");
                            list.removeAll();

                            int i=0;
                            list.add("all");
                            while(i<username.length)
                            {
                                list.add(username[i]);
                                i++;
                            }
                            String msg=(String)in.readObject();
                            textfield.setText(msg);

                            Object o=in.readObject();
                            if(o instanceof Peer)
                            {
                                peer=(Peer)o;
                            }
                            else
                            {
                                peerDao.insertPeer((Peer)o);
                            }
                            //taRecord.append("从服务器端收到的CLIENTINFO中的链表个数为:"+clientinfo.getCount()+"\n");

                        }
                        else if(type.equalsIgnoreCase("系统消息"))
                        {
                            String b=(String)in.readObject();
                            taRecord.append("系统消息"+b+"\n");
                        }
                        else if(type.equalsIgnoreCase("下线信息"))
                        {
                            String msg=(String)in.readObject();
                            taRecord.append("用户下线消息:"+msg+"\n");
                        }
                    }
                    catch(Exception e)
                    {
                        taRecord.append("error6"+e.toString());
                    }
                }
            }
            catch(Exception e)
            {
                taRecord.append("error12"+e.toString());
            }
        }

    }
    public void setIp(InetAddress ip)
    {
        this.ip=ip;
    }
    public InetAddress getIp()
    {
        return ip;
    }
    public void setPort(int port)
    {
        this.port=port;
    }
    public int getPort()
    {
        return port;
    }
//    public void setclientinfo(clientInfo clientinfo)
//    {
//        this.clientinfo=clientinfo;
//    }
//    public clientInfo getClientInfo()
//    {
//        return this.clientinfo;
//    }
    public void setSelectedPort(int port)
    {
        this.selectedPort=port;
    }
    public int getSelectedPort()
    {
        return selectedPort;
    }
}

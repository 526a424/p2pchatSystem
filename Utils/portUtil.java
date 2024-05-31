package com.example.chatsystem.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public class portUtil {

    public static int getAvailablePort(){
        Random rand = new Random();
        while(true){
            try{
                int port = rand.nextInt(65535);
                ServerSocket socket = new ServerSocket(port);
                socket.close();
                return port;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

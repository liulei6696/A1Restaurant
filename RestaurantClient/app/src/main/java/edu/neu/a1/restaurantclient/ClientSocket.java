package edu.neu.a1.restaurantclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {

    private static Socket socket;

    public static Socket getSocket(){
        if(socket == null){
            try{
                socket = new Socket("10.0.2.2", 8080);
            }catch (UnknownHostException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }finally{
                if(socket != null){
                    try{
                        socket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        return socket;
    }

}

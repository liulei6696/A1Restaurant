package edu.neu.a1.restaurantclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Servlet {

    Socket socket = ClientSocket.getSocket();
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;

    public Servlet(){ // constructor to get input stream and output stream from socket
        try{
            inputStream = (ObjectInputStream) socket.getInputStream();
            outputStream = (ObjectOutputStream) socket.getOutputStream();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendOut(final Order order){

        new SendOrderThread(order).start();

    }

    public Order get(){

        Order newOrder = null;

        try{
            newOrder = (Order) inputStream.readObject();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return newOrder;
    }


    private class SendOrderThread extends Thread{

        Order order;

        SendOrderThread(Order order){
            this.order = order;
        }

        @Override
        public void run() {
            try {
                outputStream.writeObject(order);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

//    private class GetOrderThread extends Thread{
//
//        Order newOrder = null;
//
//        GetOrderThread(Order order){
//            newOrder = order;
//        }
//
//        @Override
//        public void run() {
//            try{
//                newOrder = (Order) inputStream.readObject();
//            }catch (IOException e){
//                e.printStackTrace();
//            }catch (ClassNotFoundException e){
//                e.printStackTrace();
//            }
//        }
//    }

}

package edu.neu.a1.restaurantclient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Servlet { // send and receive order as strings

    Socket socket = ClientSocket.getSocket();
    BufferedReader in;
    PrintWriter out;

    public Servlet(){ // constructor to get input stream and output stream from socket
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendOut(final Order order){

        new SendOrderThread(order).start();

    }

    public String get(){

        String order = null;

        try{
            order = in.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }

        return order;
    }


    private class SendOrderThread extends Thread{

        Order order;

        SendOrderThread(Order order){
            this.order = order;
        }

        @Override
        public void run() {

            out.write(order.toString());

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

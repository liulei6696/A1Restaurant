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

    static Socket socket = ClientSocket.getSocket();
    static BufferedReader in;
    static PrintWriter out;
    static Servlet servlet;

    private Servlet(){ // constructor to get input stream and output stream from socket
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Servlet getServlet(){
        if(servlet == null){
            servlet = new Servlet();
        }

        return servlet;
    }

    public void sendOut(String order){

        new SendOrderThread(order).start();

    }

    public String get(){

        String order = null;

        try{
            while((order = in.readLine())!=null);
        }catch (IOException e){
            e.printStackTrace();
        }

        return order;
    }


    private class SendOrderThread extends Thread{

        String order;

        SendOrderThread(String order){
            this.order = order;
        }

        @Override
        public void run() {

            out.write(order);
            out.flush();

        }
    }


}

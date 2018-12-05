package edu.neu.a1.restaurantclient;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GetReceiptTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String order;

    public GetReceiptTask(Context context, String order) {
        this.context = context;
        this.order = order;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try{
            // establish socket connection, send out order
            Socket socket = new Socket("10.0.2.2", 8080);
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.write(order);
            pw.flush();
            socket.shutdownOutput();

            // receive receipt from server
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String backOrder = null;
            while((backOrder = bufferedReader.readLine())!=null){
                new DatabaseManagerThread(context, backOrder).run(); // write to database

            }

            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}

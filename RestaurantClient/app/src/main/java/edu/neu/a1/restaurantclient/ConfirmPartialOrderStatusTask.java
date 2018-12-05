package edu.neu.a1.restaurantclient;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConfirmPartialOrderStatusTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String order;

    ConfirmPartialOrderStatusTask(Context context, String order){
        this.context = context;
        this.order = order;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try{

            new DatabaseManagerThread(context, order).run(); // write to file

            System.out.println("confirm and write to database");

            // establish socket connection and send out order
            Socket socket = new Socket("10.0.2.2", 8080);
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.write(order);
            pw.flush();
            socket.shutdownOutput();

            // receive info from server
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String backOrder = null;
            while((backOrder=bufferedReader.readLine())!=null){
                new DatabaseManagerThread(context, backOrder).run();
                if(backOrder.split(",")[7].equalsIgnoreCase(OrderStatus.OnProcess.toString()))
                    new GetReceiptTask(context, backOrder).execute();
            }

            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}

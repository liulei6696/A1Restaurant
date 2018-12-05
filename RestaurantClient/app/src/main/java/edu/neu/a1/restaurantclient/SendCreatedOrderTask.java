package edu.neu.a1.restaurantclient;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SendCreatedOrderTask extends AsyncTask<Void, Void, Void> {

    private String order;
    private Context context;

    SendCreatedOrderTask(Context context, String order){
        this.order = order;
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... s) {

        try{
            // establish socket connection, send out order
            Socket socket = new Socket("10.0.2.2", 8080);
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.write(order);
            pw.flush();
            socket.shutdownOutput();

            // receive info from server
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String backOrder = null;
            while((backOrder = bufferedReader.readLine())!=null){
                new DatabaseManagerThread(context, backOrder).run(); // write to database

                // switch to getReceipt depending on returned order status
                if(backOrder.split(",")[7].equalsIgnoreCase(OrderStatus.OnProcess.toString()))
                    new GetReceiptTask(context, backOrder).execute();
                // if order status is wait, handle it in edit order activity
            }

            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


}

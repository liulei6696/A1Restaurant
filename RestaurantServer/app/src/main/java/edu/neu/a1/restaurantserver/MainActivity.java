package edu.neu.a1.restaurantserver;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    File file;
    FileReader fileReader;
    FileWriter fileWriter;
    BufferedReader bufferedReader;
    FileOutputStream fileOutputStream;
    FileInputStream fileInputStream;
    OutputStreamWriter outputStreamWriter;
    InputStreamReader inputStreamReader;
    ServerSocket serverSocket;
    ArrayAdapter arrayAdapter2=null;
    boolean flag=true;
    private static int OrderID=0;
    static InventoryController inventoryController;
    static Set<ConnectionToClient> set=new HashSet<>();
    static OrderList orderList=new OrderList();
    static  Kitchen kitchen=new Kitchen();
    ListView listView;
    final Context context = this;

    InventoryListController inventoryListController;
    OrderListController orderListController;
    String msg = null; // message to be displayed on screen
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        System.out.println("1111111"+Environment.getExternalStorageDirectory().toString());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1_listview);

        // find element on layout
        ScrollView scrollView = findViewById(R.id.scroll_view);
        textView = findViewById(R.id.text_view_in_scroll_view);
        textView.setTextColor(getColor(R.color.primary_black));

        // initialize inventory list
        inventoryListController = new InventoryListController(2,10,10,10);

        // initialize order list controller to store and read order
        orderListController = new OrderListController();

        msg = "initial " + inventoryListController.getDescription() + "\n\n";
        textView.setText(msg);

        new ServerSocketThread().start();

        // start consuming order
        new KitchenThread(inventoryListController, orderListController).start();

        // TODO: initialize read inventory in thread

//        // initialize inventory list
//        if(flag){
//            try {
//                fileOutputStream=openFileOutput("inventory.txt",Context.MODE_PRIVATE);
//                outputStreamWriter=new OutputStreamWriter(fileOutputStream);
//                outputStreamWriter.write("Burgers 50"+'\n');
//                outputStreamWriter.write("Chickens 50"+'\n');
//                outputStreamWriter.write("French Fries 50"+'\n');
//                outputStreamWriter.write("Onion Rings 50");
//                outputStreamWriter.flush();
//                fileInputStream=openFileInput("inventory.txt");
//                inputStreamReader=new InputStreamReader(fileInputStream);
//                flag=false;
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


//        inventoryController=new InventoryController(inputStreamReader,outputStreamWriter);
//        Semaphore semaphore=new Semaphore(4);
//        Kitchen kitchen=new Kitchen();

//        final ScheduledExecutorService scheduler=Executors.newScheduledThreadPool(1);
//        final ScheduledFuture<?> UpdateInventoryController =scheduler.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                inventoryController.Update();
//            }
//        },60,60,TimeUnit.HOURS);
//        scheduler.schedule(new Runnable() {
//            @Override
//            public void run() {
//                UpdateInventoryController.cancel(true);
//                scheduler.shutdown();
//            }
//        },60,TimeUnit.HOURS);

//        Thread main=new ServerSocketThread();
//        main.start();
//
//        listView= findViewById(R.id.ListView);
//        arrayAdapter2= new ArrayAdapter(context,android.R.layout.simple_list_item_1,orderList.getOrderList());
//        listView.setAdapter(arrayAdapter2);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent = new Intent(context, OrderDetailsActivity.class);
//                Bundle bundle=new Bundle();
//                String idString=listView.getItemAtPosition(position).toString().substring(10,15).trim();
//                System.out.println(idString);
//
//                if(idString!=null) {
//                    intent.putExtra("id",idString);
//                    startActivity(intent);
//                }
//            }
//        });
    }

    /**
     * initialize server socket on port 8080
     * @throws IOException if port is in use
     */
    private void buildSocket() throws  IOException{
        serverSocket=new ServerSocket(8080);
    }

    @Override
    protected void onDestroy() {
        try {
            fileReader.close();
            fileWriter.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    private class ServerSocketThread extends Thread {

        @Override
        public void run() {
            try{
                buildSocket();
                while (true){

                    // get order information from client
                    Socket socket = serverSocket.accept();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String order = null;
                    while ((order = bufferedReader.readLine())!=null) {
                        new DisplayOrderThread(order).start(); // refresh display
                        String[] array = order.split(",");

                        // carry on operations depending on order status
                        if (array[7].equalsIgnoreCase(Status.created.toString())) {
                            orderListController.addOrder(order); // add order to order list
                            if (checkInventory(order))
                                new OnProcessOrderReplyThread(socket, order).start();
                            else
                                new WaitOrderReplyThread(socket, order).start();
                        } else if (array[7].equalsIgnoreCase(Status.confirmed.toString())) {
                            new OnProcessOrderReplyThread(socket, order).start();
                        } else if (array[7].equalsIgnoreCase(Status.canceled.toString())) {
                            new CancelOrderReplyThread(socket, order).start();
                        } else if (array[7].equalsIgnoreCase(Status.onProcess.toString())) {
                            new SendReceiptReplyThread(socket, order).start();
                        }
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * refresh display on screen thread
     */
    private class DisplayOrderThread extends Thread {

        String[] array;

        DisplayOrderThread(String order){
            array = order.split(",");
        }

        @Override
        public void run() {
            synchronized (msg){
                msg += new StringBuffer().append("Order#: ").append(array[0])
                        .append("    status: ").append(array[7].toUpperCase())
                        .append("\n").append("details: ").append("burger: ")
                        .append(array[2]).append(" chicken: ").append(array[3])
                        .append(" fries: ").append(array[4]).append(" rings: ")
                        .append(array[5]).append("   Total $ ").append(array[6]).append("\n")
                        .append("current ").append(inventoryListController.getDescription()).append("\n\n");

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(msg);
                    }
                });
            }
        }
    }


    /**
     * check if the inventory is enough for order
     * @param order order string
     * @return true if inventory is enough for order
     */
    private boolean checkInventory(String order){
        String[] array = order.split(",");
        int[] list = inventoryListController.getInventoryList();

        return (Integer.parseInt(array[2])<=list[0]) && (Integer.parseInt(array[3])<=list[1])
                && (Integer.parseInt(array[4])<=list[2]) && (Integer.parseInt(array[5])<=list[3]);
    }

    /**
     * add partial details on existing order at the end by order [bugNum, chickNum, friesNum, ringsNum]
     * @param order order string
     * @return order with additional partial prepare details
     */
    private String addPartialDetail(String order){

        String[] array = order.split(",");
        int[] list = inventoryListController.getInventoryList();
        StringBuffer sb = new StringBuffer();
        sb.append(order);

        for(int i=0; i<4; i++) {
            sb.append(",").append(Integer.parseInt(array[i+2]) > list[i] ? list[i] : Integer.parseInt(array[i+2]));
        }

        return sb.toString();
    }


    /**
     * change status to OnProcess, send order out, close socket and display order
     */
    private class OnProcessOrderReplyThread extends Thread {

        String order;
        Socket socket;

        OnProcessOrderReplyThread(Socket socket, String order){
            this.socket = socket;
            this.order = order;
        }

        @Override
        public void run() {
            // change order status and update order in order list, display updated order
            String replyOrder = order.substring(0,order.lastIndexOf(",")) + "," + Status.onProcess.toString();
            orderListController.updateOrder(replyOrder);
            new DisplayOrderThread(replyOrder);

            // reply to client
            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.write(replyOrder);
                printWriter.flush();

                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * change order status to Wait, send order to client, close socket and display order
     * add additional information on order
     */
    private class WaitOrderReplyThread extends Thread {

        String order;
        Socket socket;

        WaitOrderReplyThread(Socket socket, String order){
            this.order = order;
            this.socket = socket;
        }

        @Override
        public void run() {
            // change order status and update order in order list, display updated order
            String replyOrder = order.substring(0,order.lastIndexOf(",")) + "," + Status.wait.toString();
            orderListController.updateOrder(replyOrder);
            new DisplayOrderThread(replyOrder).start();

            replyOrder = addPartialDetail(replyOrder);

            // reply to client
            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.write(replyOrder);
                printWriter.flush();

                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * remove order from order list through updateOrder(), display order, close socket
     */
    private class CancelOrderReplyThread extends Thread{

        Socket socket;
        String order;

        CancelOrderReplyThread(Socket socket, String order){
            this.socket = socket;
            this.order = order;
        }

        @Override
        public void run() {

            orderListController.updateOrder(order);
            new DisplayOrderThread(order).start();

            try{
                socket.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * check if the order is in queue, if not, which means it's ready, send order receipt
     */
    private class SendReceiptReplyThread extends Thread{

        Socket socket;
        String order;

        SendReceiptReplyThread(Socket socket, String order){
            this.socket = socket;
            this.order = order;
        }

        @Override
        public void run() {
            // sleep if the order isn't complete
            while(orderListController.contains(order)){
                try{
                    sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            // change order status and send it to client
            String replyOrder = order.substring(0,order.lastIndexOf(",")) + "," + Status.complete.toString();
            new DisplayOrderThread(replyOrder).start();

            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.write(replyOrder);
                printWriter.flush();

                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


//    /**
//     * Thread that accept connection from client and reply
//     */
//    private class ServerSocketThread extends Thread {
//
//        @Override
//        public void run() {
//            try {
//                buildSocket();
//                while (true) {
//                    Socket socket = serverSocket.accept();
//                    try {
//                        final ConnectionToClient cur = new ConnectionToClient(socket);
//                        Thread ReceiverOrderContinually = new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                while (true) {
//                                    try {
//                                        if (cur.ReceiveOrderDetail() != null) {
//                                            int id = getOrderID();
//                                            Order order = cur.ReceiveOrder(id);
//                                            orderList.addOrder(order,cur);
//                                            cur.SendOrder(order);
//                                            arrayAdapter2.add(order);
//                                            arrayAdapter2.notifyDataSetChanged();
//                                        }
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    } catch (ClassNotFoundException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            }
//                        });
//                        ReceiverOrderContinually.start();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//    synchronized public int getOrderID(){
//        MainActivity.OrderID++;
//        return OrderID;
//    }




}

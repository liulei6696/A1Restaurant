package edu.neu.a1.restaurantserver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("1111111"+Environment.getExternalStorageDirectory().toString());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1_listview);
        if(flag){
            try {
                fileOutputStream=openFileOutput("inventory.txt",Context.MODE_PRIVATE);
                outputStreamWriter=new OutputStreamWriter(fileOutputStream);
                outputStreamWriter.write("Burgers 50"+'\n');
                outputStreamWriter.write("Chickens 50"+'\n');
                outputStreamWriter.write("French Fries 50"+'\n');
                outputStreamWriter.write("Onion Rings 50");
                outputStreamWriter.flush();
                fileInputStream=openFileInput("inventory.txt");
                inputStreamReader=new InputStreamReader(fileInputStream);
                flag=false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        inventoryController=new InventoryController(inputStreamReader,outputStreamWriter);
        Semaphore semaphore=new Semaphore(4);
        Kitchen kitchen=new Kitchen();

        final ScheduledExecutorService scheduler=Executors.newScheduledThreadPool(1);
        final ScheduledFuture<?> UpdateInventoryController =scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                inventoryController.Update();
            }
        },60,60,TimeUnit.HOURS);
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                UpdateInventoryController.cancel(true);
                scheduler.shutdown();
            }
        },60,TimeUnit.HOURS);

        Thread main=new ServerSocketThread();
        main.start();

        listView= findViewById(R.id.ListView);
        arrayAdapter2= new ArrayAdapter(context,android.R.layout.simple_list_item_1,orderList.getOrderList());
        listView.setAdapter(arrayAdapter2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(context, OrderDetailsActivity.class);
                Bundle bundle=new Bundle();
                String idString=listView.getItemAtPosition(position).toString().substring(10,15).trim();
                System.out.println(idString);

                if(idString!=null) {
                    intent.putExtra("id",idString);
                    startActivity(intent);
                }
            }
        });
    }

    private void buildSocket() throws  IOException{
        serverSocket=new ServerSocket(888);
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

        ServerSocketThread() {

        }

        public void run() {
            try {
                buildSocket();
                while (true) {
                    Socket socket = null;
                    try {
                        if(serverSocket.accept()!=null) {
                            socket = serverSocket.accept();
                            final ConnectionToClient cur = new ConnectionToClient(socket);
                            Thread ReceiverOrderContinually = new Thread(new Runnable() {


                                @Override
                                public void run() {
                                    while (true) {
                                        try {
                                            if (cur.ReceiveOrderDetail() != null) {
                                                int id = getOrderID();
                                                Order order = cur.ReceiveOrder(id);
                                                orderList.addOrder(order,cur);
                                                cur.SendOrder(order);
                                                arrayAdapter2.add(order);
                                                arrayAdapter2.notifyDataSetChanged();
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            });
                            ReceiverOrderContinually.start();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    synchronized public int getOrderID(){
        MainActivity.OrderID++;
        return OrderID;
    }




}

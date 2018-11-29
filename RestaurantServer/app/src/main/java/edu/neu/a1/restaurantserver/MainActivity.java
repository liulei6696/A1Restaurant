package edu.neu.a1.restaurantserver;

import android.content.Context;
import android.content.Intent;
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    File file=new File("Inventory.txt");
    FileReader fileReader;
    FileWriter fileWriter;
    BufferedReader bufferedReader;
    ServerSocket serverSocket;

    {
        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static int OrderID=0;
    static InventoryController inventoryController;
    static Set<ConnectionToClient> set=new HashSet<>();
    static OrderList orderList=new OrderList();
    static  Kitchen kitchen=new Kitchen();
    Button button;
    TextView order1;
    TextView order2;

    //ScrollView scrollView=new ScrollView(this);
    //TextView textView=new TextView(this);

    ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        try {
            fileReader=new FileReader(file);
            fileWriter=new FileWriter(file);
            bufferedReader=new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inventoryController=new InventoryController(bufferedReader,fileWriter);

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

        Thread main=new ServerSocketThread(serverSocket);
        main.start();
        addListenerOnButton();
        addListenerOnButton2();
        order1=findViewById(R.id.textView2);
        order2=findViewById(R.id.textView3);

        //Initiate list view
        listView=(ListView) findViewById(R.id.ListView);
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,orderList.getOrderList());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Navigate to "Order Details" page.
            }
        });
    }

    public void addListenerOnButton() {

        //scrollView.addView(textView);
        final Context context = this;

        button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, OrderDetailsActivity.class);
                Bundle bundle=new Bundle();
                //get order id from textview
                int id=Integer.parseInt((String) order1.getText());
                //get complete order by connectiontoclient
                Order order=orderList.getOrder(id);
                if(order!=null){
                    bundle.putParcelable("Order", order);
                    bundle.putSerializable("Map",order.getItemsMap());
                    bundle.putString("Status",order.getStatus().toString());
                    startActivity(intent);
                }

            }

        });
    }
    public void addListenerOnButton2() {

            final Context context = this;

            button = (Button) findViewById(R.id.button2);

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Intent intent = new Intent(context, OrderDetailsActivity.class);
                    startActivity(intent);

                }

            });

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
        private ServerSocket serverSocket;

        ServerSocketThread(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }


        public void run() {
            while (true) {
                Socket socket = null;
                try {
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
                                        order1.append(Integer.toString(order.getOrderId()));
                                        MainActivity.orderList.getClientAndOrderMap().get(order).SendOrder(order);
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
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }
    synchronized public int getOrderID(){
        MainActivity.OrderID++;
        return OrderID;
    }


}

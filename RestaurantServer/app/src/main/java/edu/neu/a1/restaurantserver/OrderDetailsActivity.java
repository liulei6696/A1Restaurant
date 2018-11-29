package edu.neu.a1.restaurantserver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class OrderDetailsActivity extends Activity{

    TextView msg, orderId, burger, chicken, status, fries, onionrings;
    ScrollView check;
    Button confirm, cancelorder, back;
    Order order;
    int id;
    ConnectionToClient connectionToClient;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        burger = (TextView)findViewById(R.id.burger);
        msg = (TextView)findViewById(R.id.msg);
        orderId = (TextView)findViewById(R.id.orderId);
        chicken = (TextView)findViewById(R.id.chicken);
        fries = (TextView)findViewById(R.id.fries);
        onionrings = (TextView)findViewById(R.id.onionrings);
        status = (TextView)findViewById(R.id.status);
        confirm = (Button)findViewById(R.id.confirm);
        cancelorder = (Button)findViewById(R.id.cancel);
//        check=(ScrollView) findViewById(R.id.check);

        //getorderdetail
        id= (int) getIntent().getExtras().get("ID");
        order=MainActivity.orderList.getOrder(id);
        orderId.setText(id);

        /*
        这里
         */
        //get connectiontoclient and order
//        connectionToClient=MainActivity.orderList.getClient(order.getOrderId());
//
//        boolean enough=CheckIfEnough();
//        if(enough){
//            TextView textView=new TextView(this);
//            textView.setText("Enough stock");
//            check.addView(textView);
//
//        }
//        else {
//            TextView textView=new TextView(this);
//            Order modifiedOrder=order;
//            modifiedOrder.setItemsMap((HashMap<Item, Integer>) MainActivity.inventoryController.ModifyOrder(order.getItemsMap()));
//            textView.append(Integer.toString(order.getOrderId()));
//            Map<Item,Integer> Itemsdetail=modifiedOrder.getItemsMap();
//            for(Item i:Itemsdetail.keySet()){
//                StringBuilder s=new StringBuilder();
//                s.append(i.getName()).append("       ");
//                s.append(Itemsdetail.get(i));
//                textView.append(s.toString());
//            }
//            connectionToClient.SendPartialNotificatin(modifiedOrder);
//        }

        addListenerOnConfirm();
        addListenerOnBack();
    }

    private boolean CheckIfEnough() {
        return MainActivity.inventoryController.IfEnough(order.getItemsMap());
    }

    public void addListenerOnConfirm(){

        final Context context = this;
        confirm = (Button) findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionToClient.SendOrder(order);
                int id=order.getOrderId();
                Thread order=new SingleOrderThread(MainActivity.orderList,new Semaphore(4),id,MainActivity.kitchen);
                if(!MainActivity.orderList.getOrder(id).isBlocked()||MainActivity.orderList.getOrder(id).getStatus()==Status.canceled){
                    order.start();
                }
                try {
                    order.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
//        confirm.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//                Intent intent = new Intent(context, MainActivity.class);
//                startActivity(intent);
//
//            }
//
//        });
    }

    public void addListenerOnBack(){

        final Context context = this;

        back = (Button) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

            }

        });
    }
}

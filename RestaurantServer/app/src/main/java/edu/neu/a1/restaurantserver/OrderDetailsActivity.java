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
    Button confirm, cancelorder, back, sendModifyRequest;
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
        sendModifyRequest = (Button)findViewById(R.id.sendRequest);


        //getorderdetail
        String id1=getIntent().getStringExtra("id");
        id=Integer.parseInt(id1);
        order=MainActivity.orderList.getOrder(id);
        orderId.setText(id1);
        init();

        if(order.getItemsMap()!=null){
            for(Item i : order.getItemsMap().keySet()) {
                System.out.println(i.getName());
                if (i.getName().contains("Burger")) {
                    burger.setText("burger: " + order.getItemsMap().get(i));
                } else if (i.getName().contains("Chicken")) {
                    chicken.setText("Chicken: " + order.getItemsMap().get(i));
                } else if (i.getName().contains("French fries")) {
                    fries.setText("French Fries: " + order.getItemsMap().get(i));
                } else if (i.getName().contains("Onion Ring")) {
                    onionrings.setText("Onion Rings: " + order.getItemsMap().get(i));
                }
            }
        }
          status.setText("status: "+MainActivity.orderList.getOrder(id).getStatus());
        connectionToClient=MainActivity.orderList.getClient(order.getOrderId());


        boolean enough=CheckIfEnough();
        if(enough){
            TextView textView=new TextView(this);
            textView.setText("Enough stock");
            check.addView(textView);

        }
        else {
            TextView textView=new TextView(this);
            Order modifiedOrder=order;
            modifiedOrder.setItemsMap((HashMap<Item, Integer>) MainActivity.inventoryController.ModifyOrder(order.getItemsMap()));
            textView.append(Integer.toString(order.getOrderId()));
            Map<Item,Integer> Itemsdetail=modifiedOrder.getItemsMap();
            if(Itemsdetail!=null){
                for(Item i:Itemsdetail.keySet()){
                    StringBuilder s=new StringBuilder();
                    s.append(i.getName()).append("       ");
                    s.append(Itemsdetail.get(i));
                    textView.append(s.toString());
                }
            }
            else {
                textView.append("Empty Order");
            }

        }

        addListenerOnConfirm();
        addListenerOnBack();
        addListenerOnSendModifyRequest();
        addListenerOnCancelOrder();
    }

    private void init() {
        burger.setText("burgers: "+0);
        chicken.setText("Chiecken: "+0);
        fries.setText("French Fires: "+0);
        onionrings.setText("Onion Rings: "+0);
    }

    private boolean CheckIfEnough() {
        if(order.getItemsMap()!=null){
            return MainActivity.inventoryController.IfEnough(order.getItemsMap());
        }
        return false;
    }

    public void addListenerOnSendModifyRequest(){
        final Context context = this;
        sendModifyRequest = (Button) findViewById(R.id.sendRequest);

        sendModifyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckIfEnough()== false){
                    connectionToClient.SendPartialNotificatin(order);
                }
            }
        });
    }
    public void addListenerOnCancelOrder(){
        final Context context = this;
        cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setStatus(Status.canceled);
            }
        });
    }


    public void addListenerOnConfirm(){

        final Context context = this;
        confirm = (Button) findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionToClient.SendOrder(order);
                int id=order.getOrderId();
                Thread orderT=new SingleOrderThread(MainActivity.orderList,new Semaphore(4),id,MainActivity.kitchen);
                if(!MainActivity.orderList.getOrder(id).isBlocked()||MainActivity.orderList.getOrder(id).getStatus()==Status.canceled){
                    orderT.start();
                }
                else {
                    connectionToClient.SendOrder(order);
                }
                try {
                    orderT.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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

package edu.neu.a1.restaurantclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class MainPageActivity extends AppCompatActivity {

    TextView burgerNum, chickenNum, friesNum, onionRingNum, totalPrice;

    // initialize customer
    Customer lei = new Customer("Lei Liu", 1443309);
    Customer dong = new Customer("Andong Wang", 1407537);
    Customer ling = new Customer("Dingling Ge", 1493622);

//    final static Item burgers = new Burger();
//    final static Item chickens = new Chickens();
//    final static Item fries = new FrenchFries();
//    final static Item onionRing = new OnionRing();

    // initialize order map for customer
    Order order = new Order(lei.getId());

    // servlet
//    Servlet servlet = new Servlet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // find text views
        burgerNum = findViewById(R.id.burgerNum);
        chickenNum = findViewById(R.id.chickenNum);
        friesNum = findViewById(R.id.friesNum);
        onionRingNum = findViewById(R.id.onionRingNum);
        totalPrice = findViewById(R.id.totalPrice);

        // find add and minus buttons in layout and set listener
        Button addBurgers = findViewById(R.id.plusBurger);
        addBurgers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.editItem("burger", 1);
                MainPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        burgerNum.setText(order.getItemNum("burger")+"");
                    }
                });
            }
        });
        Button minusBurgers = findViewById(R.id.minusBurger);
        minusBurgers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.editItem("burger",-1);
                MainPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        burgerNum.setText(order.getItemNum("burger")+"");
                    }
                });
            }
        });
        Button addChickens = findViewById(R.id.plusChicken);
        addChickens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.editItem("chicken",1);
                MainPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chickenNum.setText(order.getItemNum("chicken")+"");
                    }
                });
            }
        });
        Button minusChickens = findViewById(R.id.minusChicken);
        minusChickens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.editItem("chicken", -1);
                MainPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chickenNum.setText(order.getItemNum("chicken")+"");
                    }
                });
            }
        });
        Button addFries = findViewById(R.id.plusFries);
        addFries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.editItem("fries", 1);
                MainPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        friesNum.setText(order.getItemNum("fries")+"");
                    }
                });
            }
        });
        Button minusFires = findViewById(R.id.minusFries);
        minusFires.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.editItem("fries",-1);
                MainPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        friesNum.setText(order.getItemNum("fries")+"");
                    }
                });
            }
        });
        Button addOnionRings = findViewById(R.id.plusOnionRing);
        addOnionRings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.editItem("onionRing", 1);
                MainPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onionRingNum.setText(order.getItemNum("onionRing")+"");
                    }
                });
            }
        });
        Button minusOnionRings = findViewById(R.id.minusOnionRing);
        minusOnionRings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.editItem("onionRing", -1);
                MainPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onionRingNum.setText(order.getItemNum("onionRing")+"");
                    }
                });
            }
        });


        // find other buttons
        Button placeOrder = findViewById(R.id.placeOrder);
        Button orderList = findViewById(R.id.orderList);
        Button customer = findViewById(R.id.customer);

        placeOrder.setOnClickListener(PlaceOrderOnClickListener);

        orderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });
    }

//    private class ConnectToServerThread extends Thread{
//
//        @Override
//        public void run() {
//            try{
//                socket = new Socket("10.0.2.2", 8080);
//            }catch (UnknownHostException e){
//                e.printStackTrace();
//            }catch (IOException e){
//                e.printStackTrace();
//            }finally{
//                if(socket != null){
//                    try{
//                        socket.close();
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }



    private class SendOrderAndRefreshOrderThread extends Thread{

        @Override
        public void run() {

//            servlet.sendOut(order);
//            order = servlet.get();

            Intent intent = new Intent(MainPageActivity.this, EditOrderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("order",order);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    View.OnClickListener PlaceOrderOnClickListener = new View.OnClickListener() { // send order to server and send order to next page
        @Override
        public void onClick(View v) {

//            new SendOrderAndRefreshOrderThread().run();

            Intent intent = new Intent(MainPageActivity.this, EditOrderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("order",order);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

}

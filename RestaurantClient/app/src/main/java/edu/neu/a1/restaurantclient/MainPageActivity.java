package edu.neu.a1.restaurantclient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import edu.neu.a1.restaurantclient.item.Burger;

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

        // run at the first time, don't run at the second time
//        clearBufferedData();

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
                new UpdateTotalPriceThread().start();
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
                new UpdateTotalPriceThread().start();
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
                new UpdateTotalPriceThread().start();
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
                new UpdateTotalPriceThread().start();
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
                new UpdateTotalPriceThread().start();
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
                new UpdateTotalPriceThread().start();
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
                new UpdateTotalPriceThread().start();
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
                new UpdateTotalPriceThread().start();
            }
        });


        // find other buttons
        Button placeOrder = findViewById(R.id.placeOrder);
        Button orderList = findViewById(R.id.orderList);
        Button customer = findViewById(R.id.customer);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                new SendOrderAndRefreshOrderThread(MainPageActivity.this).run();

                saveOrderStringToFile(order.toString(), MainPageActivity.this);

                Intent intent = new Intent(MainPageActivity.this, EditOrderActivity.class);
                intent.putExtra("orderString", order.toString());
                startActivity(intent);
            }
        });

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

    class SendOrderAndRefreshOrderThread extends Thread{

        Context context;

        public SendOrderAndRefreshOrderThread(Context context){
            this.context = context;
        }

        @Override
        public void run() {
            Servlet.getServlet().sendOut(order.toString());
            updateOrderInFile(Servlet.getServlet().get());
        }

        private void updateOrderInFile(String order){
            StringBuffer sb = new StringBuffer();
            try {
                // read out contents
                FileInputStream inOrderList = context.openFileInput("order_list.csv");
                InputStreamReader inOrderListReader = new InputStreamReader(inOrderList, "UTF-8");
                BufferedReader br = new BufferedReader(inOrderListReader);
                String line;
                while ((line = br.readLine()) != null) {
                    if(match(line, order)){
                        sb.append(order).append("\n");
                    }else
                        sb.append(line).append("\n");
                }
                // write in order
                FileOutputStream outputStream = context.openFileOutput("order_list.csv", MODE_PRIVATE);
                outputStream.write(sb.toString().getBytes());
                outputStream.flush();

            }catch (IOException e){
                e.printStackTrace();
            }
        }

        private boolean match(String old, String a){
            String[] olds = old.split(",");
            String[] as = a.split(",");

            if(olds[0].equals("-1"))
                if(olds[2].equals(as[2])&&olds[3].equals(as[3])&&olds[4].equals(as[4])&&olds[5].equals(as[5]))
                    return true;

            return false;
        }
    }

    private void clearBufferedData(){

        // initialize application data file using file in raw folder, will clear history
        try{
            FileOutputStream outputStream = openFileOutput("order_list.csv", MODE_PRIVATE);
            InputStream rawIn = getResources().openRawResource(R.raw.orders_list);
            BufferedReader br = new BufferedReader(new InputStreamReader(rawIn,"UTF-8"));
            String line;
            while((line=br.readLine())!=null){
                byte[] bytes = (line+"\n").getBytes();
                outputStream.write(bytes);
                outputStream.flush();
            }
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    private void saveOrderStringToFile(String order, Context context){

        try {
//            // read out contents
//            FileInputStream inOrderList = context.openFileInput("order_list.csv");
//            InputStreamReader inOrderListReader = new InputStreamReader(inOrderList, "UTF-8");
//            BufferedReader br = new BufferedReader(inOrderListReader);
//            String line;
//            while((line=br.readLine())!=null){
//                sb.append(line);
//            }
//            sb.append(order);

            // write in order
            FileOutputStream outputStream = context.openFileOutput("order_list.csv", MODE_APPEND);
            outputStream.write((order+"\n").getBytes());
            outputStream.flush();

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    private class UpdateTotalPriceThread extends Thread {

        double tp =(order.getItemNum("burger")*5.0 +order.getItemNum("chicken")*7.0
                +order.getItemNum("fries")*2.5+order.getItemNum("onionRing")*3.5)*1.3;

        @Override
        public void run(){
            MainPageActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    totalPrice.setText(new DecimalFormat("#.00").format(tp));
                }
            });
        }
    }

}

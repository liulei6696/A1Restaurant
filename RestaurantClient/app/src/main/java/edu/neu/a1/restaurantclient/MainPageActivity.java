package edu.neu.a1.restaurantclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class MainPageActivity extends AppCompatActivity {

    static boolean clearHistory = true;

    TextView burgerNum, chickenNum, friesNum, onionRingNum, totalPrice;

    // initialize customer
    Customer lei = new Customer("Lei Liu", 1443309);
    Customer dong = new Customer("Andong Wang", 1407537);
    Customer ling = new Customer("Dingling Ge", 1493622);

    // initialize order map for customer
    Order order = new Order(lei.getId()); // TODO change it to create order when ready to send order


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // run at the first time, don't run at the second time
        if(clearHistory) {
            clearBufferedData();
            clearHistory = false;
        }

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

        // on click listener for "place order"
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // save order to database
                new DatabaseManagerThread(MainPageActivity.this, order.toString()).run();

                // send out order using AsyncTask
                new SendCreatedOrderTask(MainPageActivity.this, order.toString()).execute();

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


    /**
     *  initialize application data file using file in "raw/orders_list.csv", will clear history
     *
     */
    private void clearBufferedData(){


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


    /**
     * Thread that updates total price
     *
     */
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

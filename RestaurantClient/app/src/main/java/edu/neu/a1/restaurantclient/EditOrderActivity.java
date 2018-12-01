package edu.neu.a1.restaurantclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditOrderActivity extends Activity {

    TextView burg, chick, fries, rings, Status, totalPrice, editOrder;
    Button confirm, cancel, orderList, main;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        burg = (TextView)findViewById(R.id.BurgNum);
        chick = (TextView)findViewById(R.id.ChickNum);
        fries = (TextView)findViewById(R.id.FriesNum);
        rings = (TextView)findViewById(R.id.RingsNum);
        totalPrice = findViewById(R.id.edit_order_totalPrice);
        editOrder = findViewById(R.id.order_id);

        new RefreshQuantitiesTextViewsThread(getIntent()).start();


        Status = (TextView)findViewById(R.id.Status);

        confirm = (Button)findViewById(R.id.confirm);
        cancel = (Button)findViewById(R.id.cancel);

        //Status with confirm and cancel buttons
        Status.setText("pending");
        confirm.setTag(false);
        cancel.setTag(false);
        final boolean flag =(boolean) confirm.getTag();
        final boolean flag2 =(boolean) cancel.getTag();
        confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (!flag2 && !flag) {
                    Status.setText("confirmed");
                    confirm.setTag(true);
                }
            }});
        cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (!flag2) {
                    Status.setText("cancelled");
                    cancel.setTag(true);
                }
            }});

        //interface jump
        addListenerOnOrderList();
        addListenerOnMain();
    }

    public void addListenerOnOrderList(){

        final Context context = this;

        orderList = (Button) findViewById(R.id.orderList);

        orderList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, OrderListActivity.class);
                startActivity(intent);

            }

        });
    }

    public void addListenerOnMain(){

        final Context context = this;

        main = (Button) findViewById(R.id.main);

        main.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, MainPageActivity.class);
                startActivity(intent);

            }

        });
    }

    private class RefreshQuantitiesTextViewsThread extends Thread{

        Intent intent;

        RefreshQuantitiesTextViewsThread(Intent intent){
            this.intent = intent;
        }

        @Override
        public void run() {
//            Order order = (Order) intent.getSerializableExtra("order");
            String order = intent.getStringExtra("orderString");
            String[] strs = order.split(",");

            burg.setText(strs[2]);
            chick.setText(strs[3]);
            fries.setText(strs[4]);
            rings.setText(strs[5]);
            totalPrice.setText(strs[6]);
            Status.setText(strs[7]);

            if(strs[0].equals("-1")){editOrder.setText(" ");}
            else editOrder.setText(strs[0]);

        }
    }
}


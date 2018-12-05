package edu.neu.a1.restaurantclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class EditOrderActivity extends Activity {

    TextView burg, chick, fries, rings, Status, totalPrice, editOrder, prompt, partialDetail;
    Button confirm, cancel, orderList, main;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        // find text views
        burg = (TextView)findViewById(R.id.BurgNum);
        chick = (TextView)findViewById(R.id.ChickNum);
        fries = (TextView)findViewById(R.id.FriesNum);
        rings = (TextView)findViewById(R.id.RingsNum);
        Status = (TextView)findViewById(R.id.Status);
        totalPrice = findViewById(R.id.edit_order_totalPrice);
        editOrder = findViewById(R.id.order_id);

        // partial order related
        prompt = findViewById(R.id.prompt);
        partialDetail = findViewById(R.id.partial_detail);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);

        // thread that set text views
        new RefreshQuantitiesTextViewsThread(getIntent()).start();


        //Status with confirm and cancel buttons
        Status.setText("Sent");

        // button confirm on click listener
        confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                confirm.setClickable(false);
                cancel.setClickable(false);

                // update order
                String oldOrder = getIntent().getStringExtra("orderString");
                StringBuilder sb = new StringBuilder();
                String[] array = oldOrder.split(",");
                double total = (Double.parseDouble(array[8])*5.0 + Double.parseDouble(array[9])*7.0 +
                        Double.parseDouble(array[10])*2.5 + Double.parseDouble(array[11])*3.5)*1.3;
                sb.append(array[0]).append(",").append(array[1]).append(",")
                        .append(array[8]).append(",").append(array[9]).append(",")
                        .append(array[10]).append(",").append(array[11]).append(",")
                        .append((new DecimalFormat("0.00").format(total)) + ",")
                        .append(OrderStatus.Confirmed.toString());

                // start confirm order task, update database and send it to server
                new ConfirmPartialOrderStatusTask(EditOrderActivity.this, sb.toString()).execute();
            }});

        // button cancel on click listener
        cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                confirm.setClickable(false);
                cancel.setClickable(false);

                // update order
                String oldOrder = getIntent().getStringExtra("orderString");
                StringBuilder sb = new StringBuilder();
                String[] array = oldOrder.split(",");
                double total = (Double.parseDouble(array[8])*5.0 + Double.parseDouble(array[9])*7.0 +
                        Double.parseDouble(array[10])*2.5 + Double.parseDouble(array[11])*3.5)*1.3;
                sb.append(array[0]).append(",").append(array[1]).append(",")
                        .append(array[8]).append(",").append(array[9]).append(",")
                        .append(array[10]).append(",").append(array[11]).append(",")
                        .append((new DecimalFormat("0.00").format(total)) + ",")
                        .append(OrderStatus.Canceled.toString());

                // start confirm order task and send it to server
                new ConfirmPartialOrderStatusTask(EditOrderActivity.this, sb.toString()).execute();
            }});

        //interface jump
        addListenerOnOrderList();
        addListenerOnMain();

    }


    /**
     * add onClick listener on button to order list
     */
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

    /**
     * add onClick listener on button back to main page
     */
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


    /**
     * update layout according to the order received, will hide buttons and text views accordingly
     */
    private class RefreshQuantitiesTextViewsThread extends Thread{

        Intent intent;

        RefreshQuantitiesTextViewsThread(Intent intent){
            this.intent = intent;
        }

        @Override
        public void run() {
            String order = intent.getStringExtra("orderString");
            String[] strs = order.split(",");

            burg.setText(strs[2]);
            chick.setText(strs[3]);
            fries.setText(strs[4]);
            rings.setText(strs[5]);
            totalPrice.setText(strs[6]);
            Status.setText(strs[7]);
            editOrder.setText(strs[0]);

            if(strs[7].equalsIgnoreCase(OrderStatus.Wait.toString())){
                prompt.setVisibility(View.VISIBLE);
                partialDetail.setVisibility(View.VISIBLE);
                partialDetail.setText("Burger: "+strs[8]+", Chicken: "+strs[9]+", Fries: "+strs[10]+", Rings: "+strs[11]);
                confirm.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                confirm.setClickable(true);
                cancel.setClickable(true);
            }
            else {
                prompt.setVisibility(View.GONE);
                partialDetail.setVisibility(View.GONE);
                confirm.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
            }

        }
    }
}


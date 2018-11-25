package edu.neu.a1.restaurantclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditOrderActivity extends Activity {
    // TODO: third page, edit order

    TextView Burg, Chick, Fries, Rings, Status;
    Button confirm, cancel, orderList, main;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order); // TODO: layout for this page
        Burg = (TextView)findViewById(R.id.Burg);
        Chick = (TextView)findViewById(R.id.Chick);
        Fries = (TextView)findViewById(R.id.Fries);
        Rings = (TextView)findViewById(R.id.Rings);
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
}


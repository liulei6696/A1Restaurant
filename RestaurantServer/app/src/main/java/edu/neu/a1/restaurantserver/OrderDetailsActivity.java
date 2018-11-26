package edu.neu.a1.restaurantserver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderDetailsActivity extends Activity{

    TextView msg, orderId, burger, chicken, status, fries, onionrings;
    Button confirm, cancelorder, back, sendRequest;
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
        sendRequest = (Button)findViewById(R.id.sendRequest);


        addListenerOnConfirm();
        addListenerOnBack();
        addListenerOnSendRequest();
    }

    public void addListenerOnConfirm(){

        final Context context = this;

        confirm = (Button) findViewById(R.id.confirm);

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

    public void addListenerOnSendRequest(){

        final Context context = this;

        sendRequest = (Button) findViewById(R.id.sendRequest);

        sendRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

//                Intent intent = new Intent(context, MainActivity.class);
//                startActivity(intent);

            }

        });
    }
}

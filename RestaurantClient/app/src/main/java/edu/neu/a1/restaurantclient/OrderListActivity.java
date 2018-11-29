package edu.neu.a1.restaurantclient;

import android.app.Activity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OrderListActivity extends Activity {

    private InputStream inOrderList;
    // TODO: button and text views on layout

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list); // TODO: layout for this page

        // TODO: read files and display order list

        try {
            inOrderList = getResources().openRawResource(R.raw.orders_list);
            InputStreamReader inOrderListReader = new InputStreamReader(inOrderList,"UTF-8");
            BufferedReader br = new BufferedReader(inOrderListReader);
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    private class DisplayOrderThread extends Thread{

        String order;

        DisplayOrderThread(String order){
            this.order = order;
        }

        @Override
        public void run() {
            String[] strs = order.split(",");
            int orderId = Integer.parseInt(strs[0]);
            int customer = Integer.parseInt(strs[1]);
            int burgNum = Integer.parseInt(strs[2]);
            int chickNum = Integer.parseInt(strs[3]);
            int friesNum = Integer.parseInt(strs[4]);
            int ringNum = Integer.parseInt(strs[5]);
            double totalPrice = Double.parseDouble(strs[6]);
            String status = strs[7];

            // TODO: update view RunOnUiThread()
        }
    }
}

package edu.neu.a1.restaurantclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends Activity {

    private FileInputStream inOrderList;
    private ListView container;
    private Button backMainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        container = findViewById(R.id.order_list);
        backMainPage = findViewById(R.id.order_list_to_main_page);


        List<String> orderList = new ArrayList<>();
        try {
            inOrderList = openFileInput("order_list.csv");
            InputStreamReader inOrderListReader = new InputStreamReader(inOrderList,"UTF-8");
            BufferedReader br = new BufferedReader(inOrderListReader);
            String order = br.readLine();
            while((order = br.readLine())!=null){
                orderList.add(order);
//                LinearLayout child = findViewById(R.id.line);
//                child.setText(order);
//                container.addView(child);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(getApplicationContext(), orderList);
        container.setAdapter(myBaseAdapter);

        // back to main page button
        backMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderListActivity.this, MainPageActivity.class);
                startActivity(intent);
            }
        });

    }


    // display adapter for list view of order list page
    private class MyBaseAdapter extends BaseAdapter{

        Context mContext;
        List<String> orders;

        public MyBaseAdapter(Context mContext, List<String> orders){
            this.mContext = mContext;
            this.orders = orders;
        }

        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public Object getItem(int position) {
            return orders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.order_detail_line,null);

            TextView order_id = view.findViewById(R.id.line_order_id);
            TextView order_status = view.findViewById(R.id.line_order_status);
            TextView order_burge = view.findViewById(R.id.line_burger_num);
            TextView order_chick = view.findViewById(R.id.line_chick_num);
            TextView order_fries = view.findViewById(R.id.line_fries_num);
            TextView order_ring = view.findViewById(R.id.line_ring_num);
            TextView order_price = view.findViewById(R.id.line_total_price);
            Button edit = view.findViewById(R.id.line_edit);

            final String order = orders.get(position);
            String[] strs = order.split(",");

            if(strs[0].equals("-1")) order_id.setText(" ");
            else order_id.setText(strs[0]);
            order_status.setText(strs[7]);
            if(strs[7].equalsIgnoreCase(OrderStatus.Wait.toString()))
                order_status.setTextColor(getResources().getColor(R.color.colorRed));
            order_burge.setText(strs[2]);
            order_chick.setText(strs[3]);
            order_fries.setText(strs[4]);
            order_ring.setText(strs[5]);
            order_price.setText(strs[6]);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EditOrderActivity.class);
                    intent.putExtra("orderString", order);
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}

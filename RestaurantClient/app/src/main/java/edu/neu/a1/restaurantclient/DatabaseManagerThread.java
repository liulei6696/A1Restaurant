package edu.neu.a1.restaurantclient;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DatabaseManagerThread extends Thread{

    private Context context;
    private String order;

    DatabaseManagerThread(Context context, String order){
        this.context = context;
        this.order = order;
    }

    @Override
    public void run() {
        write(order);
    }

    private synchronized void write(String order){
        try {
            if(exists(order))
                update(order);
            else
                add(order);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void add(String order){
        try {
            FileOutputStream outputStream = context.openFileOutput("order_list.csv", Context.MODE_APPEND);
            outputStream.write((order + "\n").getBytes());
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void update(String order){
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
            FileOutputStream outputStream = context.openFileOutput("order_list.csv", Context.MODE_PRIVATE);
            outputStream.write(sb.toString().getBytes());
            outputStream.flush();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * check if two order string have the same id
     * @param old order string in database
     * @param a updated order string
     * @return true if two order is the same order
     */
    private boolean match(String old, String a){ // if order exists

        return (old.split(",")[0].equals(a.split(",")[0]));

    }


    /**
     * check if the order exists in database, by id
     * @param order order to be checked
     * @return true if exists
     * @throws IOException caused by reading file
     */
    private boolean exists(String order) throws IOException{

        FileInputStream inOrderList = context.openFileInput("order_list.csv");
        InputStreamReader inOrderListReader = new InputStreamReader(inOrderList, "UTF-8");
        BufferedReader br = new BufferedReader(inOrderListReader);
        String line;
        while((line = br.readLine()) != null){
            if(match(line, order)) {
                return true;
            }
        }
        return false;
    }
}

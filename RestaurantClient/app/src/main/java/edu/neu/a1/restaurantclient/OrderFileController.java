package edu.neu.a1.restaurantclient;

import android.app.Activity;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class OrderFileController {

    static OrderFileController orderFileController;
    FileOutputStream out;
    FileInputStream in;

    private OrderFileController(){
    }

    public static OrderFileController getOrderFileController(){
        if(orderFileController == null){
            orderFileController = new OrderFileController();
        }
        return orderFileController;
    }

    public void write(String order){

    }
}

package edu.neu.a1.restaurantclient;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private String name;
    private int id;
    private List<Order> orderList = new ArrayList<>();

    public Customer(String name, int id){
        this.name = name;
        this.id = id;
    }

//    public String getName(){
//        return name;
//    }
//
//    public void setName(String name){
//        this.name = name;
//    }

    public int getId(){
        return id;
    }

//    public void setId(int id){
//        this.id = id;
//    }

    public void addOrder(Order order){
        orderList.add(order);
//        return orderList;
    }

    public List<Order> getOrderList(){
        return orderList;
    }
}

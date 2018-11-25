package edu.neu.a1.restaurantclient;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    // TODO: add attributes and methods
    String name;
    int id;
    List orderList = new ArrayList<Order>();

    public String getname(){
        return name;
    }

    public void setname(String name){
        this.name = name;
    }

    public int getid(){
        return id;
    }

    public void setid(int id){
        this.id = id;
    }

    public List addOrder(Order order){
        orderList.add(order);
        return orderList;
    }
}

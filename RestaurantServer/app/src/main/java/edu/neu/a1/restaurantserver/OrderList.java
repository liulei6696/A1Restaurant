package edu.neu.a1.restaurantserver;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderList {

    private ArrayList<Order> orderList;

    public OrderList(){
        this.orderList=new ArrayList<>();
    }

    public ArrayList<Order> getOrderList(){
        return this.orderList;
    }

    public void addOrder(HashMap<Item,Integer> map){
        Order order =new Order(map);
        orderList.add(order);
    }

    public void removeOrder(Order order){
        orderList.remove(order);
    }

}

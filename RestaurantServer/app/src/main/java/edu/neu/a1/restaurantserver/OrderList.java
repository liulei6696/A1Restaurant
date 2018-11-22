package edu.neu.a1.restaurantserver;

import java.util.ArrayList;

public class OrderList {

    private ArrayList<Order> orderList;

    public OrderList(){
        this.orderList=new ArrayList<>();
    }

    public ArrayList<Order> getOrderList(){
        return this.orderList;
    }

    public void addOrder(Order order){
        orderList.add(order);
    }

    public void removeOrder(Order order){
        orderList.remove(order);
    }

}

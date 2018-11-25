package edu.neu.a1.restaurantclient;

import java.util.HashMap;

import edu.neu.a1.restaurantclient.item.Item;

public class Order {

    private HashMap<Item, Integer> itemsMap;
    private int customerId;
    private String orderId;
    private OrderStatus status;

    public Order(HashMap<Item, Integer> items, int customerId){ // constructor, set status as Created
        this.itemsMap = items;
        this.customerId = customerId;
        status = OrderStatus.Created;
    }

    public HashMap<Item, Integer> getItems(){
        return itemsMap;
    }

    public OrderStatus getStatus(){
        return status;
    }

    public void setStatus(OrderStatus newStatus){
        status = newStatus;
    }

    public void setOrderId(String id){ // called by server side after generate an order id
        orderId = id;
    }


}

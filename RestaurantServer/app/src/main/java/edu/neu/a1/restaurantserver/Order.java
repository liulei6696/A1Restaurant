package edu.neu.a1.restaurantserver;

import java.util.HashMap;

public class Order {

    private int orderId;
    private static int count=1;
    private String customerName;
    private HashMap<Item,Integer> itemsMap;
    private Enum status;

    public Order(HashMap<Item,Integer> map) {
        itemsMap=map;
        orderId=count++;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Enum getStatus() {
        return status;
    }

    public void setStatus(Enum status) {
        this.status = status;
    }

}

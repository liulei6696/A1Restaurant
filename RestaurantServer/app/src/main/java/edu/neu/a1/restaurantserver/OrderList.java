package edu.neu.a1.restaurantserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderList {

    private ArrayList<Order> orderList;
    private Map<Integer,Order> orderMap=new HashMap<>();
    private Map<Integer,ConnectionToClient> ClientAndOrderMap=new HashMap<>();

    public Map<Integer, ConnectionToClient> getClientAndOrderMap() {
        return ClientAndOrderMap;
    }

    public OrderList(){
        this.orderList=new ArrayList<>();
    }

    public ArrayList<Order> getOrderList(){
        return this.orderList;
    }

    public void removeOrder(Order order){
        orderList.remove(order);
    }

    public void addOrder(Order order,ConnectionToClient connectionToClient) {
        orderList.add(order);
        orderMap.put(order.getOrderId(),order);
        ClientAndOrderMap.put(order.getOrderId(),connectionToClient);
    }

    public Order getOrder(int id){
        return orderMap.get(id);
    }
    public ConnectionToClient getClient(int id){
        return ClientAndOrderMap.get(id);
    }
}

package edu.neu.a1.restaurantclient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.neu.a1.restaurantclient.item.Burger;
import edu.neu.a1.restaurantclient.item.Chickens;
import edu.neu.a1.restaurantclient.item.FrenchFries;
import edu.neu.a1.restaurantclient.item.Item;
import edu.neu.a1.restaurantclient.item.OnionRing;

public class Order implements Serializable {

    private Map<Item, Integer> itemsMap;
    private int customerId;
    private String orderId;
    private OrderStatus status;

    private final Item burger = new Burger();
    private final Item chicken = new Chickens();
    private final Item fries = new FrenchFries();
    private final Item onionRings = new OnionRing();

    public Order(int customerId){ // constructor, set status as Created

        itemsMap = new HashMap<>();
        itemsMap.put(burger, 0);
        itemsMap.put(chicken, 0);
        itemsMap.put(fries, 0);
        itemsMap.put(onionRings, 0);

        this.customerId = customerId;
        status = OrderStatus.Created;
    }

    public Map<Item, Integer> getItems(){
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

    public void editItem(String item, int num){
        if(item.equals("burger")) {
            int newNum = itemsMap.get(burger)+num;
            newNum = newNum > 0 ? newNum : 0;
            itemsMap.put(burger, newNum);
        }
        else if(item.equals("chicken")) {
            int newNum = itemsMap.get(chicken)+num;
            newNum = newNum > 0 ? newNum : 0;
            itemsMap.put(chicken, newNum);
        }
        else if(item.equals("fries")) {
            int newNum = itemsMap.get(fries)+num;
            newNum = newNum > 0 ? newNum : 0;
            itemsMap.put(fries, newNum);
        }
        else if(item.equals("onionRing")) {
            int newNum = itemsMap.get(onionRings)+num;
            newNum = newNum > 0 ? newNum : 0;
            itemsMap.put(onionRings, newNum);
        }
    }

    public int getItemNum(String itemName){
        if(itemName.equals("burger")) return itemsMap.get(burger);
        if(itemName.equals("chicken")) return itemsMap.get(chicken);
        if(itemName.equals("fries")) return itemsMap.get(fries);
        if(itemName.equals("onionRing")) return itemsMap.get(onionRings);

        return 0;
    }
}

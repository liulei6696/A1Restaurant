package edu.neu.a1.restaurantserver;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Order implements Parcelable {

    private int orderId;
    private static int count=1;
    private int customerId;
    private HashMap<Item,Integer> itemsMap;
    private Status status;
    private  boolean isBlocked=false;
    private double price=0;


    public  Order(){
        status=Status.created;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public Order(HashMap<Item,Integer> map) {
        itemsMap=map;
        orderId=count++;
        status=Status.created;
    }

    public HashMap<Item, Integer> getItemsMap() {
        return itemsMap;
    }

    public void setItemsMap(HashMap<Item, Integer> itemsMap) {
        this.itemsMap = itemsMap;
    }

    public void setStatus(String name){
        if(name.equalsIgnoreCase("created")) status=Status.created;
        else if(name.equalsIgnoreCase("wait")) status=Status.wait;
        else if(name.equalsIgnoreCase("canceled")) status=Status.canceled;
        else if(name.equalsIgnoreCase("onProcess")) status=Status.onProcess;
        else if(name.equalsIgnoreCase("ready")) status=Status.ready;
    }

    protected Order(Parcel in) {
        orderId = in.readInt();
        customerId=in.readInt();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public int getOrderId() {
        return orderId;
    }


    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderId);
        dest.writeInt(customerId);
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString(){
        return "Order ID: "+Integer.toString(orderId) + "       Status: " + status ;
    }
    public static Order convertOrder(String orderString){
        Order returnOrder=new Order();
        String[] order=orderString.split(",");
        returnOrder.setOrderId(Integer.parseInt(order[0].trim()));
        returnOrder.setCustomerId(Integer.parseInt(order[1].trim()));
        Map<Item,Integer> map=new HashMap<>();
        map.put(new Burger(),Integer.parseInt(order[2].trim()));
        map.put(new Chicken(),Integer.parseInt(order[3].trim()));
        map.put(new FrenchFries(),Integer.parseInt(order[4].trim()));
        map.put(new OnionRing(),Integer.parseInt(order[5].trim()));
        returnOrder.setItemsMap((HashMap<Item, Integer>) map);
        returnOrder.price=Double.parseDouble(order[6].trim());
        returnOrder.setStatus(order[7].trim());
        return returnOrder;
    }
    public static String packageOrder(Order order){
        StringBuilder s=new StringBuilder();
        int bur=0,chi=0,fre=0,oni=0;
        for(Item i:order.getItemsMap().keySet()){
            if(i.getName().contains("Burger")){
                bur=order.getItemsMap().get(i);
            }
            else if(i.getName().contains("Chicken")){
                chi=order.getItemsMap().get(i);
            }
            else if(i.getName().contains("FrenchFries")){
                fre=order.getItemsMap().get(i);
            }
            else if(i.getName().contains("OnionRing")){
                oni=order.getItemsMap().get(i);
            }
        }
        s.append(order.getOrderId()).append(",").append(order.getCustomerId()).append(",").append(bur).append(",").append(chi).append(",").append(fre).append(",").append(oni).append(",").append(order.getPrice()).append(",").append(order.getStatus().toString());
        return s.toString();
    }
}

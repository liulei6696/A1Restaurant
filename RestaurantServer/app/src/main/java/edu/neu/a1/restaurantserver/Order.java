package edu.neu.a1.restaurantserver;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Order implements Parcelable {

    private int orderId;
    private static int count=1;
    private String customerName;
    private HashMap<Item,Integer> itemsMap;
    private Status status;
    private  boolean isBlocked=false;


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
        customerName = in.readString();
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
        dest.writeString(customerName);
    }

    @Override
    public String toString(){
        return "Order ID: "+Integer.toString(orderId) + "   Status: " + status ;
    }

}

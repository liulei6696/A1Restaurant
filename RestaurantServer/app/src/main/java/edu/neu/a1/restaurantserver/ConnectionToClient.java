package edu.neu.a1.restaurantserver;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ConnectionToClient {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Map<Item, Integer> map = new HashMap<>();
    private Order order;


    ConnectionToClient(Socket socket) throws IOException {
        this.socket = socket;
        in = new ObjectInputStream(this.socket.getInputStream());
        out = new ObjectOutputStream(this.socket.getOutputStream());
        order.setOrderId(-1);
    }

    //Order is the one we can't prepare enough food for clients so we can change the order into the
    //one we can prepare for out clients and see if client can accept this new order
    public void SendPartialNotificatin(Order ModifiedOrder) {
        if (ModifiedOrder!=null) WriteMessages(ModifiedOrder);
    }

    //Iniatialy, when we receive the order detail, the order is not built because we have to check
    //if we have enough inventory stocks and see if we have to prepare partially.
    //When we make sure we can safely build an order, it means the checking process is finished
    public boolean CheckIfIsCompleteClient() {
        if (order.getOrderId() != -1) return true;
        return false;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        if (order != null) this.order = order;
    }

    public void WriteMessages(Order order) {
        try {
            if(order!=null) {
                out.writeObject(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Send a complete order
    public  void  SendOrder(Order order){
        if(CheckIfIsCompleteClient()){
            try {
                out.writeObject(order);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//
    public Order ReceiveOrderDetail() throws
            IOException, ClassNotFoundException {
        Order order=new Order();
        if(in.readObject()!=null) {
            order = (Order) in.readObject();
            if(MainActivity.orderList.getClientAndOrderMap().containsKey(order.getOrderId())){
                MainActivity.orderList.getOrder(order.getOrderId()).setItemsMap(order.getItemsMap());
            }
            else if(order.isBlocked()||order.getStatus()==Status.canceled){
                MainActivity.orderList.getOrder(order.getOrderId()).setStatus(Status.canceled);
                return null;
            }
            if (order != null) {
                return order;
            }
        }
        return null;
    }

    public Order ReceiveOrder(int id) throws IOException, ClassNotFoundException {
        if(ReceiveOrderDetail()!=null){
            order=ReceiveOrderDetail();
            order.setOrderId(id);
            setOrder(order);
            return order;
        }
        else return null;
    }


}






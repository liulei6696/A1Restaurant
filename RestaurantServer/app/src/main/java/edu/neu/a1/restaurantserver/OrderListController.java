package edu.neu.a1.restaurantserver;

import java.util.LinkedList;

public class OrderListController {

    private LinkedList<String> orderList = new LinkedList<>();


    /**
     * call on receive a created order
     * method that add order to order list
     * @param order order string to be added
     * @return true if add successful
     */
    public boolean addOrder(String order){
        return orderList.add(order);
    }

    /**
     * see the first element in list
     * @return the first order
     */
    public String peek(){
        return orderList.peek();
    }

    /**
     * remove the first order in list
     * @return removed order
     */
    public synchronized String remove(){
        return orderList.poll();
    }

    /**
     * whether order list is empty
     * @return true if list is empty
     */
    public boolean isEmpty(){
        return orderList.isEmpty();
    }

    /**
     * update order existing in queue
     * if order status is ready or canceled, dequeue that order
     * @param order updated order string
     */
    public void updateOrder(String order){
        int index = 0;
        for (; index<orderList.size(); index++){
            if (orderList.get(index).split(",")[0].equals(order.split(",")[0])) {
                if(order.split(",")[7].equalsIgnoreCase(Status.canceled.toString()) ||
                        order.split(",")[7].equalsIgnoreCase(Status.ready.toString()))
                    orderList.remove(index);
                else
                    orderList.set(index, order); //
                break;
            }
        }
    }

    /**
     *
     * @param order order string
     * @return true if order exists
     */
    public boolean contains(String order){
        return orderList.contains(order);
    }
}

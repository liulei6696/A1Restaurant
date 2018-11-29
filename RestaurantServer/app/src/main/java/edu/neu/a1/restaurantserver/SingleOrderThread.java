package edu.neu.a1.restaurantserver;

import java.util.concurrent.Semaphore;

class SingleOrderThread extends Thread{

    OrderList orderList=new OrderList();
    Semaphore sema;
    String threadName;
    Kitchen kitchen;
    int id;
    SingleOrderThread(OrderList orderList,Semaphore semaphore, int id, Kitchen kitchen){
        this.orderList=orderList;
        sema=semaphore;
        StringBuilder s=new StringBuilder().append("Order").append(id);
        threadName=s.toString();
        this.kitchen=kitchen;
    }
    @Override
    public void run() {
        System.out.println(threadName + " is waiting for a permit.");
        // acquiring the lock
        try {
            sema.acquire();
            orderList.getOrder(id).setStatus(Status.wait);
            orderList.getClientAndOrderMap().get(id).SendOrder(orderList.getOrder(id));
            System.out.println(threadName + " gets a permit.");
            if(orderList.getOrder(id).isBlocked()||orderList.getOrder(id).getStatus()==Status.canceled){
                Thread.interrupted();
            }
            while(this.getState()==State.WAITING) {
                if(orderList.getOrder(id).isBlocked()||MainActivity.orderList.getOrder(id).getStatus()==Status.canceled){
                    Thread.interrupted();
                }
            }
            orderList.getOrder(id).setStatus(Status.onProcess);
            orderList.getClientAndOrderMap().get(id).SendOrder(orderList.getOrder(id));
            kitchen.cook();
            //orderlist.setState()
            kitchen.prepare();
            orderList.getOrder(id).setStatus(Status.ready);
            orderList.getClientAndOrderMap().get(id).SendOrder(orderList.getOrder(id));
            sema.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
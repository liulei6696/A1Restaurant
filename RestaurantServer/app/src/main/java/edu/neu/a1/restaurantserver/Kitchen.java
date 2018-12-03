package edu.neu.a1.restaurantserver;

public class Kitchen {
    Kitchen(){

    }
    synchronized public void cook(){
        try {
            //orderlist setstate
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    synchronized public void prepare(){
        try {
            //orderlist setstate
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

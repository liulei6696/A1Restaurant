package edu.neu.a1.restaurantclient;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void order_toString(){
        Customer lei = new Customer("Lei Liu", 1443309);
        Order order = new Order(lei.getId());
        order.setOrderId(13300);
        order.editItem("burger",2);
        order.editItem("chicken",3);
        order.editItem("fries",0);
        order.editItem("onionRing",1);
        System.out.print(order);
    }
}
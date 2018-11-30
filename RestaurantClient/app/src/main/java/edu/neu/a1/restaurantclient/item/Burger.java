package edu.neu.a1.restaurantclient.item;

public class Burger implements Item {

    private final String name = "burgers";
    private final static double price = 5.0;

    @Override
    public double getPrice() {
        return price;
    }

}

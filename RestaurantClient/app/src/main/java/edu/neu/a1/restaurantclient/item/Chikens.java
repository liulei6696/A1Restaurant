package edu.neu.a1.restaurantclient.item;

public class Chikens implements Item{

    private final String name = "chickens";
    private final double price = 7.0;

    @Override
    public double getPrice() {
        return price;
    }
}

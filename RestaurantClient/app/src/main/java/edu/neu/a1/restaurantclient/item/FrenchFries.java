package edu.neu.a1.restaurantclient.item;

public class FrenchFries implements Item {

    private final String name = "french fries";
    private final double price = 2.5;

    @Override
    public double getPrice() {
        return price;
    }
}

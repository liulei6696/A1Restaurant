package edu.neu.a1.restaurantclient.item;

public class OnionRing implements Item {

    private final String name = "onion rings";
    private final static double price = 3.5;

    @Override
    public double getPrice() {
        return price;
    }
}

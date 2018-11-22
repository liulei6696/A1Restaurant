package edu.neu.a1.restaurantserver;

public class OnionRing implements Item{

    private String name=OnionRing.class.getName();
    private static final double price=3.50;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }
}

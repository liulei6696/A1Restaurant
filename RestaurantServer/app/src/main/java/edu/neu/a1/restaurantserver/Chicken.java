package edu.neu.a1.restaurantserver;

public class Chicken implements Item {

    private String name=Chicken.class.getName();
    private static final double price=7.00;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }
}

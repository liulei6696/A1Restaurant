package edu.neu.a1.restaurantserver;

public class FrenchFries implements Item{

    private String name=FrenchFries.class.getName();
    private static final double price=2.50;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

}

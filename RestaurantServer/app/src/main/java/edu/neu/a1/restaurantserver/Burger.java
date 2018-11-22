package edu.neu.a1.restaurantserver;

public class Burger implements Item{

    private String name=Burger.class.getName();
    private static final double price=5.00;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

}

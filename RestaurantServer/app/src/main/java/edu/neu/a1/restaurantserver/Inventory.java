package edu.neu.a1.restaurantserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Inventory {
    private Burger burger;
    private Chicken chicken;
    private FrenchFries frenchFries;
    private OnionRing onionRing;


    public Inventory() throws IOException {
        Map<Item,Integer> map = new HashMap<Item,Integer>();
        map.put(burger, 50);
        map.put(chicken,50);
        map.put(frenchFries,50);
        map.put(onionRing,50);
        File file=new File("Inventory.txt");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(String.valueOf(map));
        fileWriter.close();
        file.getAbsolutePath();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        


    }
}

package edu.neu.a1.restaurantserver;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InventoryController {
    Map<Item,Integer> inventory;
    BufferedReader bufferedReader;
    FileWriter fileWriter;
    InventoryController(BufferedReader bufferedReader, FileWriter fileWriter){
        this.bufferedReader=bufferedReader;
        this.fileWriter=fileWriter;
        try {
            init(this.bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("NewApi")
    public void Update(){
        for(Item items:inventory.keySet()){
            inventory.replace(items,inventory.get(items),50);
        }
        try {
            ModifyInventoryFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    synchronized public boolean IfEnough(Map<Item,Integer> order){
        for(Item i:order.keySet()){
            if(inventory.get(i)<order.get(i)) return false;
        }
        return true;
    }

    synchronized public Map<Item,Integer> ModifyOrder(Map<Item,Integer> order){
        Map<Item,Integer> modifierOrder=new HashMap<>();
        for(Item i:order.keySet()){
            if(inventory.get(i)<order.get(i)){
                if(inventory.get(i)==0) continue;
                else{
                    modifierOrder.put(i,inventory.get(i));
                }
            }
            else{
                modifierOrder.put(i,order.get(i));
            }
        }
        return modifierOrder;
    }

    synchronized public void ModifyInventoryFile() throws IOException {
        for(Item items:inventory.keySet()){
            StringBuilder s=new StringBuilder();
            s.append(items.getClass().getName()).append(" ").append(inventory.get(items));
            fileWriter.write(s.toString());
        }
        fileWriter.flush();
    }

    synchronized public int getItemNum(Item items){
        if(inventory.containsKey(items)){
            return inventory.get(items);
        }
        else return  -1;
    }

    synchronized private void init(BufferedReader bufferedReader) throws IOException {

        String s=bufferedReader.readLine();
        while (s!=null){
            if(s.contains("Burgers")){
                int indexofNum=s.lastIndexOf("Burgers")+1;
                String num=s.substring(indexofNum);
                num.trim();
                inventory.put(new Burger(),Integer.parseInt(num));
            }
            if(s.contains("Chickens")){
                int indexofNum=s.lastIndexOf("Chickens")+1;
                String num=s.substring(indexofNum);
                num.trim();
                inventory.put(new Chicken(),Integer.parseInt(num));
            }
            if(s.contains("French Fries")){
                int indexofNum=s.lastIndexOf("French Fries")+1;
                String num=s.substring(indexofNum);
                num.trim();
                inventory.put(new FrenchFries(),Integer.parseInt(num));
            }
            if(s.contains("Onion Rings")){
                int indexofNum=s.lastIndexOf("Onion Rings")+1;
                String num=s.substring(indexofNum);
                num.trim();
                inventory.put(new OnionRing(),Integer.parseInt(num));
            }
            s=bufferedReader.readLine();
        }
    }
}

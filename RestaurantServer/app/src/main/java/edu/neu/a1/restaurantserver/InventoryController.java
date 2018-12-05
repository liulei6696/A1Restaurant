package edu.neu.a1.restaurantserver;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InventoryController {

    Map<Item,Integer> inventory;
    InputStreamReader inputStreamReader;
    OutputStreamWriter outputStreamWriter;
    BufferedReader bufferedReader;


    InventoryController(InputStreamReader inputStreamReader, OutputStreamWriter outputStreamWriter){
//        fileInputStream=bufferedReader;
//        fileOutputStream=fileWriter;
        this.inputStreamReader=inputStreamReader;
        this.outputStreamWriter=outputStreamWriter;
        this.bufferedReader=new BufferedReader(inputStreamReader);
        inventory=new HashMap<>();
        try {
            init(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("NewApi")
    public void consumeItems(Map<Item,Integer> ItemMap){
        for(Item i:ItemMap.keySet()){
            if(inventory.containsKey(convert(i.toString()))){
                inventory.replace(convert(i.toString()),inventory.get(convert(i.toString()))-ItemMap.get(i));
            }
        }
        try {
            ModifyInventoryFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("NewApi")
    public void returnItems(Map<Item,Integer> ItemMap){
        for(Item i:ItemMap.keySet()){
            if(inventory.containsKey(convert(i.toString()))){
                inventory.replace(convert(i.toString()),inventory.get(convert(i.toString()))+ItemMap.get(i));
            }
        }
        try {
            ModifyInventoryFile();
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
            if(inventory.get(convert(i.getName()))<order.get(i)) return false;
        }
        return true;
    }

    synchronized public Map<Item,Integer> ModifyOrder(Map<Item,Integer> order){
       if(order!=null){
           Map<Item,Integer> modifierOrder=new HashMap<>();
           for(Item i:order.keySet()){
               if(inventory.get(convert(i.getName()))<order.get(i)){
                   if(inventory.get(convert(i.getName()))==0) continue;
                   else{
                       modifierOrder.put(convert(i.getName()),inventory.get(convert(i.getName())));
                   }
               }
               else{
                   modifierOrder.put(convert(i.getName()),order.get(i));
               }
           }
           return modifierOrder;
       }
       else {
           return null;
       }
    }

    synchronized public void ModifyInventoryFile() throws IOException {
        for(Item items:inventory.keySet()){
            StringBuilder s=new StringBuilder();
            s.append(items.getClass().getName()).append(" ").append(inventory.get(items));
            outputStreamWriter.write(s.toString());
        }
        outputStreamWriter.flush();
    }

    synchronized public int getItemNum(int index){
        return (int) inventory.values().toArray()[index];
    }

    synchronized private void init(BufferedReader bufferedReader) throws IOException {

        String s=bufferedReader.readLine();
        while (s!=null){
            if(s.contains("Burgers")){
                int indexofNum=s.indexOf("Burgers")+8;
                String num=s.substring(indexofNum);
                num.trim();
                inventory.put(new Burger(),Integer.parseInt(num));
            }
            if(s.contains("Chickens")){
                int indexofNum=s.indexOf("Chickens")+9;
                String num=s.substring(indexofNum);
                num.trim();
                inventory.put(new Chicken(),Integer.parseInt(num));
            }
            if(s.contains("French Fries")){
                int indexofNum=s.indexOf("French Fries")+13;
                String num=s.substring(indexofNum);
                num.trim();
                inventory.put(new FrenchFries(),Integer.parseInt(num));
            }
            if(s.contains("Onion Rings")){
                int indexofNum=s.indexOf("Onion Rings")+12;
                String num=s.substring(indexofNum);
                num.trim();
                inventory.put(new OnionRing(),Integer.parseInt(num));
            }
            s=bufferedReader.readLine();
        }
    }

    private Item convert(String ItemName){
        Set<Item> set=inventory.keySet();
        Item[] items=new Item[4];
        set.toArray(items);
        if(ItemName.contains("Burger")){
            return items[0];
        }
        else if(ItemName.contains("Chicken")){
            return  items[1];
        }
        else if(ItemName.contains("FrenchFries")){
            return  items[2];
        }
        else if(ItemName.contains("OnionRing")){
            return  items[3];
        }
        return null;
    }
}

package edu.neu.a1.restaurantserver;

import java.util.concurrent.Semaphore;

public class InventoryListController {

    private int[] inventoryList = new int[4];
    private Semaphore mutex = new Semaphore(1); // only one thread can delete or add to inventory list

    public InventoryListController(int burgNum, int chickNum, int friesNum, int ringsNum) {
        inventoryList[0] = burgNum;
        inventoryList[1] = chickNum;
        inventoryList[2] = friesNum;
        inventoryList[3]= ringsNum;
    }

    /**
     *
     * @return array of int, inventory list
     */
    public int[] getInventoryList(){
        return inventoryList;
    }

    /**
     * add more to inventory list, called only by update thread
     * @param burgNum burg num
     * @param chickNum chick num
     * @param friesNum fries num
     * @param ringsNum rings num
     */
    public void addMore(int burgNum, int chickNum, int friesNum, int ringsNum){
        try{
            mutex.acquire();
            inventoryList[0] += burgNum;
            inventoryList[1] += chickNum;
            inventoryList[2] += friesNum;
            inventoryList[3] += ringsNum;
            mutex.release();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * deduct from inventory list according to order
     * @param burgNum burg num
     * @param chickNum chick num
     * @param friesNum fries num
     * @param ringsNum rings num
     */
    public void deduct(int burgNum, int chickNum, int friesNum, int ringsNum){
        try{
            mutex.acquire();
            inventoryList[0] -= burgNum;
            inventoryList[1] -= chickNum;
            inventoryList[2] -= friesNum;
            inventoryList[3] -= ringsNum;
            mutex.release();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @return description of current inventory list status
     */
    public String getDescription(){
        return "inventory list: burger: " + inventoryList[0] + ", chicken: " + inventoryList[1]
                + ", fries: " + inventoryList[2] + ", onion rings: " + inventoryList[3];
    }



}

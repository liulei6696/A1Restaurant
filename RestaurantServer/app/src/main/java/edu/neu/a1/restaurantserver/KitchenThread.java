package edu.neu.a1.restaurantserver;

public class KitchenThread extends Thread {

    InventoryListController inventoryListController;
    OrderListController orderListController;

    KitchenThread(InventoryListController inventoryListController, OrderListController orderListController){
        this.inventoryListController = inventoryListController;
        this.orderListController = orderListController;
    }

    @Override
    public void run() {
        // peek order list if order can be processed
        while(true){
            String order = orderListController.peek();
            if(order!=null && order.split(",")[7].equalsIgnoreCase(Status.onProcess.toString())){
                startCooking(order);
                updateOrderStatus(order);
            } else {
                // sleep and check again
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * start cooking, update inventory list by order, sleep for some time
     * @param order order string
     */
    private void startCooking(String order){
        // deduct from inventory list
        inventoryListController.deduct(Integer.parseInt(order.split(",")[2]), Integer.parseInt(order.split(",")[3]),
                Integer.parseInt(order.split(",")[4]), Integer.parseInt(order.split(",")[5]));
        // cooking
        try{
            sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * change order status to ready and put it in order list(will dequeue this order)
     * @param order order string
     */
    private void updateOrderStatus(String order){
        String replyOrder = order.substring(0,order.lastIndexOf(",")) + "," + Status.ready.toString();
        orderListController.updateOrder(replyOrder);
    }
}

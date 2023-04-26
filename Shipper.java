package com.mycompany.colby_edell_lab_5_integerfactory;

public class Shipper implements Runnable {
    private ps_queue<String> stringQueue;
    
    public Shipper(ps_queue<String> stringQueue) {
        this.stringQueue = stringQueue;
    }

    @Override
    public void run() {
        // Instantiate a remaining products integer to count the number of products left to ship, and declare a packaged product string to store packaged products.
        int remainingProducts = 10;
        String packagedProduct;
        
        // While there are remaining packaged products to ship, attempt to ship a product.
        while(remainingProducts > 0) { 
            // Attempt to acquire the lock on the string queue.
            if(stringQueue.getLock().tryLock()) {
                // If we succesfully acquire the lock on the string queue, attempt to ship (consume) a packaged product from the string queue.
                try {
                    packagedProduct = stringQueue.consume();
                    System.out.println("Shipping " + packagedProduct);
                    remainingProducts--;
                    stringQueue.getLock().unlock();
                }
                catch(Exception e) {
                    // If the queue is empty, there are no products to ship. Unlock the string queue and wait.
                    stringQueue.getLock().unlock();
                }
            }

            // If we cannot acquire the lock on the string queue, wait for 0.1 second before trying again.
            try {
                Thread.sleep(100);
            }
            catch(Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        } 
    }   
}

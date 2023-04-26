package com.mycompany.colby_edell_lab_5_integerfactory;

import java.util.Random;

public class Packager implements Runnable {
    // Declare integer and string ps_queues for the Packager class.
    private ps_queue<Integer> intQueue;
    private ps_queue<String> stringQueue;
    
    // The Packager accepts both integer and string ps_queue references, which are stored in the class variables we've declared above.
    public Packager(ps_queue<Integer> intQueue, ps_queue<String> stringQueue) {
        this.intQueue = intQueue;
        this.stringQueue = stringQueue;
    }

    @Override
    public void run() {
        // Declare an integer to store the product, instantiate a remaining products counter to 10, and declare a packaged product string to store a packaged product.
        int product;
        int remainingProducts = 10;
        String packagedProduct;
        
        // While there are still products to package, attempt to package a product.
        while(remainingProducts > 0) {
            // If we can acquire the integer queue lock, attempt to acquire the string queue lock. If we cannot acquire the integer lock, wait 0.1 second and try again.
            if(intQueue.getLock().tryLock()) {
                // If we successfully acquire the integer queue lock, attempt to acquire the string queue lock. If both locks are acquired, attempt to package a product.
                if(stringQueue.getLock().tryLock()) {
                    try {
                        product = intQueue.consume();
                        System.out.println("Using " + product);
                        packagedProduct = "<" + product + ">";
                        remainingProducts--;
                        System.out.println("Packaging " + product);
                        stringQueue.produce(packagedProduct);
                        stringQueue.getLock().unlock();
                        intQueue.getLock().unlock();
 
                        // Wait a random time between 0.2 and 0.7 seconds after a successful package.
                        Random rand = new Random();

                        try {
                            Thread.sleep(rand.nextInt(501) + 200);
                        }
                        catch(Exception e) {
                            System.out.println("Exception: " + e.getMessage());
                        }
                    }
                    catch(Exception e) {
                        // If the integer queue is empty, unlock the queues and wait for 0.1 second to try again.
                        intQueue.getLock().unlock();
                        stringQueue.getLock().unlock();
                    }
                }
                else {
                    // If the packager cannot get both locks, release the integer queue lock and wait 0.1 second.
                    intQueue.getLock().unlock();
                }
            }
            
            // This 0.1-second sleep is used in multiple logical scenarios.
            try {
                Thread.sleep(100);
            }
            catch(Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }                  
    }
}

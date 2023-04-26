package com.mycompany.colby_edell_lab_5_integerfactory;

import java.util.Random;

public class Assembler implements Runnable {
    // A class variable is declared to store an integer ps_queue reference.
    private ps_queue<Integer> intQueue;
    
    // The Assembler accepts an integer ps_queue in its constructor.
    public Assembler(ps_queue<Integer> intQueue) {
        this.intQueue = intQueue;
    }

    @Override
    public void run() {
        // Instantiate an integer to store the number of products to assemble, and a Random object to produce random product integers.
        int remainingProducts = 10;
        Random rand = new Random();
        
        // While we still have products to assemble, attempt to assemble a product.
        while(remainingProducts > 0) {
            // Generate a random number (0-50) to attempt to add to the integer queue.
            int randomNumber = rand.nextInt(51);
            
            // If we can acquire the integer queue lock, write the random number we've generated into the integer queue.
            if(intQueue.getLock().tryLock()) {
                try {
                    intQueue.produce(randomNumber);
                    remainingProducts--;
                    System.out.println("Producing " + randomNumber);
                    intQueue.getLock().unlock();
                    
                    // After successfully adding a number to the integer queue, sleep for a random amount of time between 0 and 0.5 second.
                    try {
                        Thread.sleep(rand.nextInt(501));
                    }
                    catch(Exception e) {
                        System.out.println("Exception: " + e.getMessage());
                    }
                }
                catch(Exception e) {
                    // If the queue is full, unlock the integer queue and wait for 0.1 second before making another attempt.
                    intQueue.getLock().unlock();
                    try {
                    Thread.sleep(100);
                    }
                    catch(Exception f) {
                        System.out.println("Exception: " + e.getMessage());
                    }
                }
            }
            else {
                try {
                    Thread.sleep(100);
                }
                catch(Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                }
            }
        } 
        
        // Print "Job Done!" once all products have been packaged.
        System.out.println("Job Done!");
    }
}

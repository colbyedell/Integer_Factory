package com.mycompany.colby_edell_lab_5_integerfactory;

import java.util.concurrent.locks.*;

public class ps_queue<T> {
    // The generic ps_queue class contains a generic data array, lock, and indices for production and consumption.
    private T[] data;
    private Lock readWriteLock;
    private int productionIndex, consumerIndex;
    
    // The ps_queue constructor instantiates the data array to have a size of 10. The constructor also instantiates the lock.
    public ps_queue() {
        data = (T[])new Object[10];
        readWriteLock = new ReentrantLock();
    }
    
    // The "produce" function adds a product (integer) to the ps_queue.
    public void produce(T product) throws Exception {
        // If the production index is equal to the consumer index - 1 (accounting for wrap-around), the queue is full and we cannot produce.
        if((productionIndex + 1) % data.length == consumerIndex) {
            throw new Exception("Queue Full");
        }
        // If the queue is not full, write the product to the appropriate index and increment the production index (accounting for wrap-around.)
        else {
            data[productionIndex] = product;
            productionIndex = (productionIndex + 1) % data.length;
        }
    }
    
    // The "consume" function "extracts" a product (integer) from the ps_queue.
    public T consume() throws Exception {
        // If the production index is equal to the consumer index, the queue is full, and there is nothing to consume.
        if(productionIndex == consumerIndex) {
            throw new Exception("Queue Empty");
        }
        // If the queue contains elements that need to be consumed, store and return the product while incrementing the consumption index (accounting for wrap-around.)
        else {
            T product = data[consumerIndex];
            consumerIndex = (consumerIndex + 1) % data.length;
            return product;
        }
    }
    
    // A simple getter method which allows us to access the private readWriteLock from other classes.
    public Lock getLock() {
        return readWriteLock;
    }
}

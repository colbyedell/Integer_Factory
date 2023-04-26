package com.mycompany.colby_edell_lab_5_integerfactory;

public class Colby_Edell_Lab_5_IntegerFactory {
    public static void main(String[] args) {
        // Two ps_queues are created: one integer queue to store products, and one String queue to store packaged products.
        ps_queue<Integer> intQueue = new ps_queue();
        ps_queue<String> stringQueue = new ps_queue();
        
        Thread[] threadPool = new Thread[3];
        threadPool[0] = new Thread(new Assembler(intQueue));
        threadPool[1] = new Thread(new Packager(intQueue, stringQueue));
        threadPool[2] = new Thread(new Shipper(stringQueue));
        
        for(int i = 0; i < 3; i++) {
            threadPool[i].start();
        }
        
        for(int i = 0; i < 3; i++) {
            try {
                threadPool[i].join();
            }
            catch(Exception e) {
                System.out.println("Exception: " + e.getMessage());
            } 
        }
    }
}

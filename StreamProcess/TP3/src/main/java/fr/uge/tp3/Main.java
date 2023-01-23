package fr.uge.tp3;

import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        var topic = "tpdrugs";
        var producer = new Producer();
        var consumer = new Consumer();

        //ExecutorService executor = Executors.newFixedThreadPool(2);


        var producerThread = new Thread(() -> producer.publishRandomPrescriptions(10, 200, topic));
        var consumerThread = new Thread(() -> consumer.read(List.of(topic)));

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        Thread.sleep(2000);
        consumerThread.interrupt();
    }
}
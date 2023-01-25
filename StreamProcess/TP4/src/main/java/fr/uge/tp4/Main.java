package fr.uge.tp4;

import fr.uge.tp4.avro.AvroSender;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        var topic = "tpcluster";
        Path schemaPath = Path.of("src/main/resources/Prescription.avsc");
        int numConsumers = 3;
        final List<ConsumerG3> consumers = new ArrayList<>();
        String groupId = "consumer-group-3";
        List<String> topicsG3 = List.of("top2");
        ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

        var sender = AvroSender.build(schemaPath);
        var producer = new Producer(sender);
        var consumer = ConsumerTop2.build(schemaPath);

        var producerThread = new Thread(() -> producer.publishRandomPrescriptions(10, 200, topic));
        var consumerThread = new Thread(() -> consumer.read(List.of(topic)));

        for (int i = 0; i < numConsumers; i++) {
            ConsumerG3 c = ConsumerG3.build(i, groupId, topicsG3, schemaPath);
            consumers.add(c);
            executor.submit(c);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (var consumer : consumers) {
                    consumer.shutdown();
                }
                executor.shutdown();
                try {
                    executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        Thread.sleep(4000);
        consumerThread.interrupt();


    }
}
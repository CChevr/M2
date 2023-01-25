package fr.uge.tp3;

import fr.uge.tp3.avro.AvroConsumer;
import fr.uge.tp3.avro.AvroSender;
import fr.uge.tp3.json.JsonConsumer;
import fr.uge.tp3.json.JsonSender;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        var topic = "tpdrugs";
        //var sender = new JsonSender();
        var sender = AvroSender.build(Path.of("src/main/resources/Prescription.avsc"));
        var producer = new Producer(sender);
        //var consumer = new JsonConsumer();
        var consumer = AvroConsumer.build(Path.of("src/main/resources/Prescription.avsc"));

        //ExecutorService executor = Executors.newFixedThreadPool(2);

        var producerThread = new Thread(() -> producer.publishRandomPrescriptions(10, 200, topic));
        var consumerThread = new Thread(() -> consumer.read(List.of(topic)));

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        Thread.sleep(4000);
        consumerThread.interrupt();
    }
}
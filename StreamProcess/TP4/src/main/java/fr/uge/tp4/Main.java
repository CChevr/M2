package fr.uge.tp4;

import fr.uge.tp4.avro.AvroConsSend;
import fr.uge.tp4.avro.AvroConsumer;
import fr.uge.tp4.avro.AvroSender;
import fr.uge.tp4.avro.AvroConsAnalyse;
import fr.uge.tp4.json.JsonConsumer;
import fr.uge.tp4.json.JsonSender;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static fr.uge.tp4.Utils.connectPostgres;

public class Main {
    private static Connection connection;

    private static void q1() throws InterruptedException {
        var topic = "tpdrugs";
        var sender = new JsonSender();
        var producer = new Producer(connection, sender);
        var consumer = new JsonConsumer();

        var producerThread = new Thread(() -> producer.publishRandomPrescriptions(10, 200, topic));
        var consumerThread = new Thread(() -> consumer.read(List.of(topic)));

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        Thread.sleep(2000);
        consumerThread.interrupt();
    }

    private static void q2() throws IOException, InterruptedException {
        var topic = "tpdrugs";
        var schemaPath = Path.of("src/main/resources/Prescription.avsc");
        var sender = AvroSender.build(schemaPath);
        var producer = new Producer(connection, sender);
        var consumer = AvroConsumer.build(schemaPath);

        var producerThread = new Thread(() -> producer.publishRandomPrescriptions(10, 200, topic));
        var consumerThread = new Thread(() -> consumer.read(List.of(topic)));

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        Thread.sleep(2000);
        consumerThread.interrupt();
    }

    private static void q3() throws IOException, InterruptedException {
        var schemaPath = Path.of("src/main/resources/Prescription.avsc");
        var topicSrc = "tpcluster";
        var topicDst = "top2";
        String groupId = "consumer-group-3-";
        int numConsumers = 3;
        var consumers = new ArrayList<AvroConsAnalyse>();
        ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

        var sender = AvroSender.build(schemaPath);
        var producer = new Producer(connection, sender);
        var consSend = AvroConsSend.build(schemaPath);

        var producerThread = new Thread(() -> producer.publishRandomPrescriptions(10, 200, topicSrc));
        var consSendThread = new Thread(() -> consSend.read(List.of(topicSrc), topicDst));

        // Lancement des threads
        producerThread.start();
        consSendThread.start();

        for (int i = 0; i < numConsumers; i++) {
            var c = AvroConsAnalyse.build(i, groupId, List.of(topicDst), schemaPath);
            consumers.add(c);
            executor.submit(c);
        }

        // Arrêt des Threads
        producerThread.join();
        Thread.sleep(2000);
        consSendThread.interrupt();

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
    }

    public static void main(String[] args) throws InterruptedException, IOException, SQLException, ClassNotFoundException {
        // Database information
        var url = "localhost:5432";
        var db = "cedric";
        var user = "cedric";
        var password = "motdepasse";

        Main.connection = connectPostgres(url, db, user, password);

        // Exercises
        q1();
        q2();
        q3();
    }
}
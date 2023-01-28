package fr.uge.tp4;

import fr.uge.tp4.avro.AvroConsSend;
import fr.uge.tp4.avro.AvroConsumer;
import fr.uge.tp4.avro.AvroSender;
import fr.uge.tp4.avro.AvroConsAnalyse;
import fr.uge.tp4.json.JsonConsumer;
import fr.uge.tp4.json.JsonSender;
import fr.uge.tp4.kstream.JsonAnonymKStream;
import fr.uge.tp4.kstream.JsonPredicateDisplayKStream;
import fr.uge.tp4.models.Prescription;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static fr.uge.tp4.Utils.*;

public class Main {
    private static Connection connection;

    private static void q1() throws InterruptedException {
        var topic = "tpdrugs";
        var prodProps = jsonKafkaProducer(List.of("localhost:9092"));
        var consProps = jsonKafkaConsumer("consumer-exo1", List.of("localhost:9092"));

        var sender = new JsonSender(prodProps);
        var producer = new Producer(connection, sender);
        var consumer = new JsonConsumer(consProps);

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
        var prodProps = avroKafkaProducer(List.of("localhost:9092"));
        var consProps = avroKafkaConsumer("consumer-exo2", List.of("localhost:9092"));

        var sender = AvroSender.build(prodProps, schemaPath);
        var producer = new Producer(connection, sender);
        var consumer = AvroConsumer.build(consProps, schemaPath);

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
        String groupId = "consumer-group-3-2";
        var prodProps = avroKafkaProducer(List.of("localhost:9092", "localhost:9093", "localhost:9094"));
        var consProps = avroKafkaConsumer("consumer-exo3",
                List.of("localhost:9092", "localhost:9093", "localhost:9094"));
        var consGroupProps = avroKafkaConsumer(groupId,
                List.of("localhost:9092", "localhost:9093", "localhost:9094"));

        int numConsumers = 3;
        var consumers = new ArrayList<AvroConsAnalyse>();
        ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

        var sender = AvroSender.build(prodProps, schemaPath);
        var producer = new Producer(connection, sender);
        var consSend = AvroConsSend.build(prodProps, consProps, schemaPath);

        var producerThread = new Thread(() -> producer.publishRandomPrescriptions(20, 200, topicSrc));
        var consSendThread = new Thread(() -> consSend.read(List.of(topicSrc), topicDst));

        // Start threads
        for (int i = 0; i < numConsumers; i++) {
            var c = AvroConsAnalyse.build(i, consGroupProps, List.of(topicDst), schemaPath);
            consumers.add(c);
            executor.submit(c);
        }
        // Time for all consumers to join the group
        Thread.sleep(400);
        producerThread.start();
        consSendThread.start();


        // Interrupt threads
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

    public static void q6() throws InterruptedException {
        var topicSrc = "tpdrugs";
        var topicDest = "tpanonym";
        var prodProps = jsonKafkaProducer(List.of("localhost:9092"));

        Predicate<Prescription> predicate = prescription -> prescription.getPrix() > 4.;

        var sender = new JsonSender(prodProps);
        var producer = new Producer(connection, sender);

        var threadProducer = new Thread(() -> producer.publishRandomPrescriptions(10, 100, topicSrc));
        var threadKStreamAnonym = new Thread(() -> {
            try {
                JsonAnonymKStream.run(topicSrc, topicDest);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        var threadKStreamDisplay = new Thread(() -> {
            try {
                JsonPredicateDisplayKStream.run(topicDest, predicate);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadKStreamDisplay.start();
        threadKStreamAnonym.start();
        Thread.sleep(400);
        threadProducer.start();
    }

    public static void main(String[] args) throws InterruptedException, IOException, SQLException, ClassNotFoundException {
        // Database information
        var url = "localhost:5432";
        var db = "cedric";
        var user = "cedric";
        var password = "motdepasse";

        Main.connection = connectPostgres(url, db, user, password);

        // Exercises
        //q1();
        //q2();
        //q3();
        q6();
    }
}
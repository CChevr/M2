package fr.uge.tp4;

import fr.uge.tp4.avro.AvroSeDes;
import fr.uge.tp4.models.Prescription;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ConsumerG3 implements Runnable {
    private final HashMap<Integer, Double> drugsCount = new HashMap<>();
    private final KafkaConsumer<String, byte[]> consumer;
    private final List<String> topics;
    private final int id;
    private final AvroSeDes<Prescription> deserializer;

    private ConsumerG3(int id,
                       String groupId,
                       List<String> topics,
                       AvroSeDes<Prescription> deserializer) {
        this.id = id;
        this.topics = topics;
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094,");
        props.put("group.id", groupId);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", ByteArrayDeserializer.class.getName());
        this.consumer = new KafkaConsumer<>(props);
        this.deserializer = deserializer;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);

            while (true) {
                Duration timeout = Duration.ofSeconds(1);
                ConsumerRecords<String, byte[]> records = consumer.poll(timeout);
                for (ConsumerRecord<String, byte[]> record : records) {
                    var prescription = deserializer.deserialize(record.value(), new Prescription());
                    drugsCount.merge(prescription.getCip(), prescription.getPrix(), Double::sum);

                    System.out.println(this.id + " on partition " + record.partition() + " : " +prescription);
                }
            }
        } catch (WakeupException e) {
            // ignore for shutdown
        } finally {
            consumer.close();
        }
    }
    public void shutdown() {
        consumer.wakeup();
    }

    public static ConsumerG3 build(int id,
                                   String groupId,
                                   List<String> topics,
                                   Path schemaPath) throws IOException {
        AvroSeDes<Prescription> serializer = AvroSeDes.build(schemaPath);

        return new ConsumerG3(id, groupId, topics, serializer);
    }
}



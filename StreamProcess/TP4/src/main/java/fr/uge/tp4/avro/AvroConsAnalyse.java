package fr.uge.tp4.avro;

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

public class AvroConsAnalyse implements Runnable {
    private final HashMap<Integer, Double> drugsCount = new HashMap<>();
    private final KafkaConsumer<String, byte[]> consumer;
    private final List<String> topics;
    private final int id;
    private final AvroSeDes<Prescription> deserializer;

    private AvroConsAnalyse(int id,
                            Properties properties,
                            List<String> topics,
                            AvroSeDes<Prescription> deserializer) {
        this.id = id;
        this.topics = topics;
        this.consumer = new KafkaConsumer<>(properties);
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

                    System.out.println(this.id + " on partition " + record.partition() + " : " +prescription + " cumul : " + drugsCount.get(prescription.getCip()) + "$");
                }
            }
        } catch (WakeupException e) {
            // ignore for shutdown
        } finally {
            consumer.close();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Consumer " + id + "\n");
        drugsCount.entrySet()
                .forEach(e -> sb.append("Drug " + e.getKey() + " : " + e.getValue() + "$\n"));
        return sb.toString();
    }

    public void shutdown() {
        System.out.println(this);
        consumer.wakeup();
    }

    public static AvroConsAnalyse build(int id,
                                        Properties properties,
                                        List<String> topics,
                                        Path schemaPath) throws IOException {
        AvroSeDes<Prescription> serializer = AvroSeDes.build(schemaPath);

        return new AvroConsAnalyse(id, properties, topics, serializer);
    }
}



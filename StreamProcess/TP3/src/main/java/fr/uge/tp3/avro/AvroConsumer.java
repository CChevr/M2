package fr.uge.tp3.avro;

import fr.uge.tp3.models.Prescription;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


public class AvroConsumer {
    private final AvroSeDes<Prescription> deserializer;

    private AvroConsumer(AvroSeDes<Prescription> deserializer) {
        Objects.requireNonNull(this.deserializer = deserializer);
    }

    private KafkaConsumer<String, byte[]> connectKafka(List<String> topics) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        properties.put("group.id", "group1");

        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(topics);
        return consumer;
    }

    public void read(List<String> topics) {
        Duration oneSecond = Duration.ofSeconds(1);

        KafkaConsumer<String, byte[]> consumer = connectKafka(topics);

        while (true) {
            if (Thread.interrupted()) {
                try {
                    consumer.close();
                } finally {
                    consumer.wakeup();
                }
                return;
            }

            ConsumerRecords<String, byte[]> records = consumer.poll(oneSecond);
            for (ConsumerRecord<String, byte[]> record : records) {
                System.out.println("================================================================");
                System.out.println(record.value());
                var prescription = deserializer.deserialize(record.value(), new Prescription());
                System.out.println(prescription);

            }
        }
    }

    public static AvroConsumer build(Path SchemaPath) throws IOException {
        Objects.requireNonNull(SchemaPath);

        AvroSeDes<Prescription> deserializer = AvroSeDes.build(SchemaPath);
        return new AvroConsumer(deserializer);
    }
}

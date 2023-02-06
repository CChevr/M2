package fr.uge.tp4.avro;

import fr.uge.tp4.models.Prescription;
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
    private final Properties properties;

    private AvroConsumer(Properties properties, AvroSeDes<Prescription> deserializer) {
        Objects.requireNonNull(this.deserializer = deserializer);
        Objects.requireNonNull(this.properties = properties);
    }

    private KafkaConsumer<String, byte[]> connectKafka(List<String> topics) {
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
                var prescription = deserializer.deserialize(record.value(), new Prescription());
                System.out.println(prescription);
            }
        }
    }

    public static AvroConsumer build(Properties properties, Path SchemaPath) throws IOException {
        Objects.requireNonNull(SchemaPath);
        Objects.requireNonNull(properties);

        AvroSeDes<Prescription> deserializer = AvroSeDes.build(SchemaPath);
        return new AvroConsumer(properties, deserializer);
    }
}

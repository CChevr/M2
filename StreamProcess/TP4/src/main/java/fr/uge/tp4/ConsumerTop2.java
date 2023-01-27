package fr.uge.tp4;

import fr.uge.tp4.avro.AvroSeDes;
import fr.uge.tp4.avro.AvroSender;
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

public class ConsumerTop2 {
    private final AvroSeDes<Prescription> serializer;
    private final String targetTopic = "top2";
    private final PrescriptionSender sender;

    private ConsumerTop2(AvroSeDes<Prescription> serializer, PrescriptionSender sender) {
        Objects.requireNonNull(this.serializer = serializer);
        Objects.requireNonNull(this.sender = sender);
    }

    private KafkaConsumer<String, byte[]> connectKafka(List<String> topics) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        properties.put("group.id", "group2aa");

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
                    consumer.wakeup();
                } finally {
                    consumer.close();
                }
                return;
            }

            ConsumerRecords<String, byte[]> records = consumer.poll(oneSecond);
            for (ConsumerRecord<String, byte[]> record : records) {
                var prescription = serializer.deserialize(record.value(), new Prescription());
                //System.out.println(prescription);
                sender.sendPrescription(prescription, targetTopic, String.valueOf(prescription.getCip()));
            }
        }
    }

    public static ConsumerTop2 build(Path schemaPath) throws IOException {
        Objects.requireNonNull(schemaPath);

        AvroSeDes<Prescription> serializer = AvroSeDes.build(schemaPath);
        var sender = AvroSender.build(schemaPath);
        return new ConsumerTop2(serializer, sender);
    }
}

package fr.uge.tp4.avro;

import fr.uge.tp4.PrescriptionSender;
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

public class AvroConsSend {
    private final AvroSeDes<Prescription> serializer;
    private final PrescriptionSender sender;
    private final Properties properties;

    private AvroConsSend(Properties properties, AvroSeDes<Prescription> serializer, PrescriptionSender sender) {
        Objects.requireNonNull(this.serializer = serializer);
        Objects.requireNonNull(this.sender = sender);
        Objects.requireNonNull(this.properties = properties);
    }

    private KafkaConsumer<String, byte[]> connectKafka(List<String> topics) {
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(topics);
        return consumer;
    }

    public void read(List<String> topicsSrc, String topicDst) {
        Duration oneSecond = Duration.ofSeconds(1);

        KafkaConsumer<String, byte[]> consumer = connectKafka(topicsSrc);

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
                sender.sendPrescription(prescription, topicDst, String.valueOf(prescription.getCip()));
            }
        }
    }

    public static AvroConsSend build(Properties sendProps, Properties consProps, Path schemaPath) throws IOException {
        Objects.requireNonNull(schemaPath);
        Objects.requireNonNull(sendProps);
        Objects.requireNonNull(consProps);

        AvroSeDes<Prescription> serializer = AvroSeDes.build(schemaPath);
        var sender = AvroSender.build(sendProps, schemaPath);

        return new AvroConsSend(consProps, serializer, sender);
    }
}

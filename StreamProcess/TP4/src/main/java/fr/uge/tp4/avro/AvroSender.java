package fr.uge.tp4.avro;

import fr.uge.tp4.PrescriptionSender;
import fr.uge.tp4.models.Prescription;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

public class AvroSender implements PrescriptionSender {
    private final KafkaProducer<String, byte[]> kafkaProducer;
    private final AvroSeDes<Prescription> serializer;

    private AvroSender(KafkaProducer<String, byte[]> kafkaProducer, AvroSeDes<Prescription> serializer) {
        Objects.requireNonNull(this.kafkaProducer = kafkaProducer);
        Objects.requireNonNull(this.serializer = serializer);
    }

    private static KafkaProducer<String, byte[]> connectKafkaProducer() {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        return new KafkaProducer<>(properties);
    }


    @Override
    public boolean sendPrescription(Prescription prescription, String topic){
        Objects.requireNonNull(prescription);
        Objects.requireNonNull(topic);

        try {
            var serialized = serializer.serialize(prescription);
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, serialized);
            kafkaProducer.send(record);
        } catch (Exception e) {
            System.out.println("Error sending : "+e);
            return false;
        }

        return true;
    }

    @Override
    public boolean sendPrescription(Prescription prescription, String topic, String key){
        Objects.requireNonNull(prescription);
        Objects.requireNonNull(topic);

        try {
            var serialized = serializer.serialize(prescription);
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, key, serialized);
            kafkaProducer.send(record);
        } catch (Exception e) {
            System.out.println("Error sending : "+e);
            return false;
        }

        return true;
    }

    @Override
    public void close() throws Exception {
        kafkaProducer.close();
    }

    public static AvroSender build(Path schemaPath) throws IOException {
        Objects.requireNonNull(schemaPath);

        AvroSeDes<Prescription> serializer = AvroSeDes.build(schemaPath);
        KafkaProducer<String, byte[]> kafkaProducer = connectKafkaProducer();

        return new AvroSender(kafkaProducer, serializer);
    }
}

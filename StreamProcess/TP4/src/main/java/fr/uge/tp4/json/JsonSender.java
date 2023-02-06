package fr.uge.tp4.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.tp4.PrescriptionSender;
import fr.uge.tp4.models.Prescription;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Objects;
import java.util.Properties;

public class JsonSender implements PrescriptionSender {
    private final ObjectMapper mapper = new ObjectMapper();
    private final KafkaProducer<String, String> kafkaProducer;

    public JsonSender(Properties properties) {
        Objects.requireNonNull(properties);
        kafkaProducer = connectKafkaProducer(properties);
    }

    private static KafkaProducer<String, String> connectKafkaProducer(Properties properties) {
        return new KafkaProducer<>(properties);
    }

    private String getJson(Prescription prescription) throws JsonProcessingException {
        return mapper.writeValueAsString(prescription);
    }

    @Override
    public boolean sendPrescription(Prescription prescription, String topic) {
        Objects.requireNonNull(prescription);
        Objects.requireNonNull(topic);

        try {
            var json = getJson(prescription);

            if (json.equals(""))
                return false;

            var record = new ProducerRecord<String, String>(topic, json);
            kafkaProducer.send(record);
            return true;
        } catch (Exception e) {
            System.out.println("Error : "+e);
            return false;
        }
    }

    @Override
    public boolean sendPrescription(Prescription prescription, String topic, String key) {
        Objects.requireNonNull(prescription);
        Objects.requireNonNull(topic);

        try {
            var json = getJson(prescription);

            if (json.equals(""))
                return false;

            var record = new ProducerRecord<>(topic, key, json);
            kafkaProducer.send(record);
            return true;
        } catch (Exception e) {
            System.out.println("Error : "+e);
            return false;
        }
    }

    @Override
    public void close() {
        kafkaProducer.close();
    }
}

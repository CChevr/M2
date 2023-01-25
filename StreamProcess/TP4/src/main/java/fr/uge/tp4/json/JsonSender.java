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

    public JsonSender() {
        kafkaProducer = connectKafkaProducer();
    }

    private static KafkaProducer<String, String> connectKafkaProducer() {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

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

    public static void main(String[] args) {
        try (var sender = new JsonSender()) {
            var topic = "tpdrugs";
            var t = new Prescription("test", "test", 1, 10, 2);
            sender.sendPrescription(t, topic);
        }catch(Exception e) {
            System.out.println(e);
        }

        //var test = new GenericData.Record("../resources/Prescription.avsc");
    }
}

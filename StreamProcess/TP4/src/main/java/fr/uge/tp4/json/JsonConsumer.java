package fr.uge.tp4.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.tp4.models.Prescription;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class JsonConsumer {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Properties properties;

    private KafkaConsumer<String, String> connectKafka(List<String> topics) {
        var consumer = new KafkaConsumer<String,String>(properties);
        consumer.subscribe(topics);
        return consumer;
    }

    public JsonConsumer(Properties properties) {
        Objects.requireNonNull(this.properties = properties);
    }

    public void read(List<String> topics) {
        Duration oneSecond = Duration.ofSeconds(1);

        KafkaConsumer<String, String> consumer = connectKafka(topics);

        while (true) {
            if (Thread.interrupted()) {
                try {
                    consumer.close();
                } finally {
                    consumer.wakeup();
                }
                return;
            }

            var records = consumer.poll(oneSecond);
            for (ConsumerRecord<String, String> record : records) {
                try {
                    var prescription = mapper.readValue(record.value(), Prescription.class);
                    System.out.println(prescription);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

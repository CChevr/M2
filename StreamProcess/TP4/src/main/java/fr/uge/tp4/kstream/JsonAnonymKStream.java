package fr.uge.tp4.kstream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.tp4.models.Prescription;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

public class JsonAnonymKStream {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static Prescription deserialize(String value) {
        try {
            System.out.println("P - deserializing : "+value);
            return mapper.readValue(value, Prescription.class);
        } catch (JsonProcessingException e) {
            System.out.println("Error deserialization: " + e);
            return null;
        }
    }

    private static String serialize(Prescription prescription) {
        try {
            System.out.println("P - serializing : "+prescription);
            return mapper.writeValueAsString(prescription);
        } catch (JsonProcessingException e) {
            System.out.println("Error serialization : "+e);
            return null;
        }
    }

    public static void run(String topicSrc, String topicDest) throws InterruptedException {
        // Configuration
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "Anonymous-prescriptions-producer");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
                Serdes.String().getClass().getName());
        StreamsConfig streamingConfig = new StreamsConfig(props);
        Serde<String> stringSerde = Serdes.String();

        // Source processor
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream(topicSrc, Consumed.with(stringSerde, stringSerde));

        // Processing processor
        KStream<String, String> anonymized = source.mapValues(JsonAnonymKStream::deserialize)
                .filter((key, value) -> null != value)
                .mapValues(Prescription::anonymize)
                .mapValues(JsonAnonymKStream::serialize)
                .filter((key, value) -> null != value);

        // Sink processor
        //anonymized.foreach((k, v) -> System.out.println(k + " " + v));
        anonymized.to(topicDest, Produced.with(stringSerde, stringSerde));
        anonymized.print(Printed.<String, String>toSysOut().withLabel("Producer"));

        KafkaStreams streams = new KafkaStreams(builder.build(), streamingConfig);
        System.out.println("start producer");
        streams.start();
        Thread.sleep(5000L);
        streams.close();
    }
}

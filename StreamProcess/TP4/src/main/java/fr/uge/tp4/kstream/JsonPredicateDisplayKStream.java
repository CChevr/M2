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

import java.util.Properties;
import java.util.function.Predicate;

public class JsonPredicateDisplayKStream {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static Prescription deserialize(String value) {
        try {
            System.out.println("D - deserializing : "+value);
            return mapper.readValue(value, Prescription.class);
        } catch (JsonProcessingException e) {
            System.out.println("Error deserialization: " + e);
            return null;
        }
    }

    public static void run(String topicSrc, Predicate<Prescription> predicate) throws InterruptedException {
        // Configuration
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "Anonymous-prescriptions-displayer");
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
        KStream<String, Prescription> anonymized = source.mapValues(JsonPredicateDisplayKStream::deserialize)
                .filter((key, value) -> null != value)
                .filter((key, value) -> predicate.test(value));

        // Sink processor
        anonymized.print(Printed.<String, Prescription>toSysOut().withLabel("Prescription"));
        anonymized.foreach((k, v) -> System.out.println(k + " " + v));

        KafkaStreams streams = new KafkaStreams(builder.build(), streamingConfig);
        System.out.println("start consumer");
        streams.start();
        Thread.sleep(5000L);
        streams.close();
    }
}

package fr.uge.tp4;

import fr.uge.tp4.avro.AvroSeDes;
import fr.uge.tp4.models.Prescription;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Utils {
    public static Connection connectPostgres(String url, String db, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://"+url+"/"+db,
                user,
                password);

        return c;
    }

    public static Properties jsonKafkaConsumer(String groupId, List<String> servers) {
        var formattedServers = String.join(",", servers);

        Properties props = new Properties();
        props.put("bootstrap.servers", formattedServers);
        props.put("group.id", groupId);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        return props;
    }

    public static Properties jsonKafkaProducer(List<String> servers) {
        var formattedServers = String.join(",", servers);

        Properties props = new Properties();
        props.put("bootstrap.servers", formattedServers);
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        return props;
    }

    public static Properties avroKafkaConsumer(String groupId, List<String> servers) {
        var formattedServers = String.join(",", servers);

        Properties props = new Properties();
        props.put("bootstrap.servers", formattedServers);
        props.put("group.id", groupId);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", ByteArrayDeserializer.class.getName());

        return props;
    }

    public static Properties avroKafkaProducer(List<String> servers) {
        var formattedServers = String.join(",", servers);

        Properties props = new Properties();
        props.put("bootstrap.servers", formattedServers);
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", ByteArraySerializer.class.getName());

        return props;
    }
}

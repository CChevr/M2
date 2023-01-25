package fr.uge.tp4;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Utils {
    public static Connection connectPostgres(String url, String db, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://"+url+"/"+db,
                user,
                password);

        return c;
    }

    public static KafkaConsumer<String, String> connectKafkaConsumer(List<String> topics) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "group1");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String,String>(properties);
        consumer.subscribe(topics);
        return consumer;
    }
}

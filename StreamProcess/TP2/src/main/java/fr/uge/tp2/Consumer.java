package fr.uge.tp2;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.List;

import static fr.uge.tp2.Utils.connectKafkaConsumer;

public class Consumer {

    private KafkaConsumer<String, String> connectKafka(List<String> topics) {
        return connectKafkaConsumer(topics);
    }

    public void read(List<String> topics) {
        boolean running = true;
        Duration oneSecond = Duration.ofSeconds(1);

        KafkaConsumer<String, String> consumer = connectKafka(topics);

        while (running) {
            if (Thread.interrupted()) {
                try {
                    consumer.close();
                } finally {
                    consumer.wakeup();
                }
                return;
            }

            ConsumerRecords<String, String> records = consumer.poll(oneSecond);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("================================================================");
                System.out.println(record);
            }
        }
    }
}

package fr.uge.tp4;

import fr.uge.tp4.json.JsonSender;
import fr.uge.tp4.kstream.JsonAnonymKStream;
import fr.uge.tp4.kstream.JsonPredicateDisplayKStream;
import fr.uge.tp4.models.Prescription;

import java.util.function.Predicate;

public class AnonymousMain {
    public static void main(String[] args) throws InterruptedException {
        var topicSrc = "tpdrugs";
        var topicDest = "tpanonym";

        Predicate<Prescription> predicate = prescription -> prescription.getPrix() > 4.;

        var sender = new JsonSender();
        var producer = new Producer(sender);

        var threadProducer = new Thread(() -> producer.publishRandomPrescriptions(10, 100, topicSrc));
        var threadKStreamAnonym = new Thread(() -> {
            try {
                JsonAnonymKStream.run(topicSrc, topicDest);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        var threadKStreamDisplay = new Thread(() -> {
            try {
                JsonPredicateDisplayKStream.run(topicDest, predicate);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadProducer.start();
        threadKStreamDisplay.start();
        threadKStreamAnonym.start();
    }
}

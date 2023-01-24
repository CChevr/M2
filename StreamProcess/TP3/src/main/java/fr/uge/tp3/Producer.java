package fr.uge.tp3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.twitter.bijection.Bijection;
import fr.uge.tp3.json.JsonSender;
import fr.uge.tp3.models.Drug;
import fr.uge.tp3.models.Pharmacy;
import fr.uge.tp3.models.Prescription;
import org.apache.avro.Schema;
import org.apache.avro.data.Json;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.File;
import java.sql.Connection;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static fr.uge.tp3.Utils.*;
import static java.lang.Math.abs;

public class Producer {
    private final Faker faker = new Faker();
    private final PrescriptionSender sender;
    private Random random;
    private Connection connection;

    Producer(PrescriptionSender sender) {
        Objects.requireNonNull(this.sender = sender);
    }

    private boolean connectPSQL() {

        var url = "localhost:5432";
        var db = "postgres";
        var user = "postgres";
        var password = "motdepasse";

        /*
        var url = "sqletud.u-pem.fr";
        var db = "cedric.chevreuil_db";
        var user = "cedric.chevreuil";
        var password = "Motdepasse";
        */
        try {
            connection = connectPostgres(url, db, user, password);
            return true;
        } catch (Exception e) {
            System.out.println("Error " + e);
            return false;
        }
    }

    private Optional<Drug> getRandomDrug() {
        try {
            if (null == connection) {
                connectPSQL();
            }

            var query = "SELECT cip, prix FROM drugs4projet ORDER BY RANDOM() LIMIT 1";
            var response = connection.prepareStatement(query).executeQuery();

            if (response.next())
                return Optional.of(new Drug(response.getInt("cip"), response.getDouble("prix")));

        } catch(Exception e) {
            System.out.println("Error " + e);
        }
        return Optional.empty();
    }

    private Optional<Pharmacy> getRandomPharmacy() {
        try {
            if (null == connection) {
                connectPSQL();
            }

            var query = "SELECT id, nom, adresse, depart, region FROM pharm4projet ORDER BY RANDOM() LIMIT 1";
            var response = connection.prepareStatement(query).executeQuery();

            if (response.next()) {
                return Optional.of(new Pharmacy(response.getInt("id"),
                        response.getString("nom"),
                        response.getString("adresse"),
                        response.getString("depart"),
                        response.getString("region")));
            }
        } catch (Exception e) {
            System.out.println("Error "+e);
        }
        return Optional.empty();
    }

    private int getRandomDiscount(int min, int max) {
        if (min > max)
            throw new IllegalArgumentException("min must be inferior to max ["+min+", "+max+"]");

        if (null == random)
            random = new Random();

        return (abs(random.nextInt())%(max-min) + min);
    }

    private Name getRandomName() {
        return faker.name();
    }

    private Optional<Prescription> getRandomPrescription() throws JsonProcessingException {
        var name = getRandomName();
        var drug = getRandomDrug();
        var pharmacy = getRandomPharmacy();
        var discount = getRandomDiscount(-10, 10);

        if (drug.isPresent() && pharmacy.isPresent()) {
            var price = (drug.get().getPrice() + drug.get().getPrice()*(discount/100));
            return Optional.of(new Prescription(
                    name.firstName(),
                    name.lastName(),
                    drug.get().getCip(),
                    price,
                    pharmacy.get().getId()));
        } else {
            return Optional.empty();
        }
    }

    private boolean sendRandomPrescription(String topic) throws JsonProcessingException {
        var prescription = getRandomPrescription();

        if (prescription.isPresent())
            return sender.sendPrescription(prescription.get(), topic);
        else
            return false;
    }

    public boolean publishRandomPrescription(String topic) {
        try {
            return sendRandomPrescription(topic);
        } catch (Exception e) {
            System.out.println("Error " + e);
            return false;
        }
    }

    public boolean publishRandomPrescriptions(int nbMessages, int delay, String topic) {
        if (0 > nbMessages)
            throw new IllegalArgumentException("nbMessage have to be positive ("+nbMessages+")");

        if (0 > delay)
            throw new IllegalArgumentException("Delay have to be positive ("+delay+")");

        try (sender) {
            for (var i = 0; i < nbMessages; i++) {
                if (!sendRandomPrescription(topic))
                    return false;
                Thread.sleep(delay);
            }
        } catch (Exception e) {
            System.out.println("Error "+e);
            return false;
        }

        return true;
    }

    public static void main(String[] args) throws JsonProcessingException {
        var sender = new JsonSender();
        var producer = new Producer(sender);
        var topic = "tpdrugs";
        var t = new Prescription("test", "test", 1, 10, 2);
        producer.sendRandomPrescription(topic);

        //var test = new GenericData.Record("../resources/Prescription.avsc");


        //System.out.println(producer.publishRandomPrescription(topic));
        //System.out.println(producer.publishRandomPrescriptions(10, 100, topic));
    }
}

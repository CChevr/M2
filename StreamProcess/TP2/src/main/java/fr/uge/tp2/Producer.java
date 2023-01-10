package fr.uge.tp2;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import fr.uge.tp2.models.Drug;
import fr.uge.tp2.models.Pharmacy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

import static fr.uge.tp2.Utils.connectKafka;
import static fr.uge.tp2.Utils.connectPostgres;
import static java.lang.Math.abs;

public class Producer {
    private final Faker faker = new Faker();
    private Random random;
    private Connection connection;
    private KafkaProducer<String, String> kafkaProducer;

    private void connectPSQL() throws SQLException, ClassNotFoundException {
        var url = "localhost:5432";
        var db = "postgres";
        var user = "postgres";
        var password = "motdepasse";

        connection = connectPostgres(url, db, user, password);
    }

    private Optional<Drug> getRandomDrug() throws SQLException, ClassNotFoundException {
        if (null == connection) {
            connectPSQL();
        }

        var query = "SELECT cip, prix FROM drugs4projet ORDER BY RANDOM() LIMIT 1";
        var response = connection.prepareStatement(query).executeQuery();

        if (response.next())
            return Optional.of(new Drug(response.getInt("cip"), response.getDouble("prix")));
        else
            return Optional.empty();
    }

    private Optional<Pharmacy> getRandomPharmacy() throws SQLException, ClassNotFoundException {
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
        } else {
            return Optional.empty();
        }
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

    private String getJson(Name name, Drug drug, Pharmacy pharmacy, int discount) {
        return "{\"nom\":\"" + name.lastName() + "\"," +
        "\"prenom\":\"" + name.firstName() + "\"," +
        "\"cip\":" + drug.getCip() + "," +
        "\"prix\":" + (drug.getPrice() + drug.getPrice()*(discount/100)) + ","+
        "\"idPharma\":" + pharmacy.getId() + "}";
    }

    private String getRandomPrescription() throws SQLException, ClassNotFoundException {
        var name = getRandomName();
        var drug = getRandomDrug();
        var pharmacy = getRandomPharmacy();
        var discount = getRandomDiscount(-10, 10);

        if (drug.isPresent() && pharmacy.isPresent()) {
            return getJson(name, drug.get(), pharmacy.get(), discount);
        } else {
            return "";
        }
    }

    public boolean publishRandomPrescription(String topic) throws SQLException, ClassNotFoundException {
        if (null == kafkaProducer)
            kafkaProducer = connectKafka();

        var prescription = getRandomPrescription();

        if (prescription.equals(""))
            return false;

        var record = new ProducerRecord<String, String>(topic, prescription);
        kafkaProducer.send(record);
        return true;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        var producer = new Producer();

        System.out.println(producer.getRandomPrescription());
    }
}

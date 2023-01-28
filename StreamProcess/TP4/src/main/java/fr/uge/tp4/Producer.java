package fr.uge.tp4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import fr.uge.tp4.json.JsonSender;
import fr.uge.tp4.models.Drug;
import fr.uge.tp4.models.Pharmacy;
import fr.uge.tp4.models.Prescription;

import java.sql.Connection;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import static fr.uge.tp4.Utils.*;
import static java.lang.Math.abs;

public class Producer {
    private final Faker faker = new Faker();
    private final PrescriptionSender sender;
    private Random random;
    private Connection connection;

    Producer(Connection connection, PrescriptionSender sender) {
        Objects.requireNonNull(this.sender = sender);
        Objects.requireNonNull(this.connection = connection);
    }

    private Optional<Drug> getRandomDrug() {
        try {
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

        if (prescription.isEmpty())
            return false;

        return sender.sendPrescription(prescription.get(), topic);
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
                Thread.sleep(delay);
                if (!sendRandomPrescription(topic)) {
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Error "+e);
            return false;
        }

        return true;
    }
}

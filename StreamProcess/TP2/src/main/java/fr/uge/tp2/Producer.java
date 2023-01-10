package fr.uge.tp2;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import fr.uge.tp2.models.Drug;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static fr.uge.tp2.Utils.connectPostgres;

public class Producer {
    private final Faker faker = new Faker();
    private Connection connection;

    private void connect() throws SQLException, ClassNotFoundException {
        var url = "localhost:5432";
        var db = "postgres";
        var user = "postgres";
        var password = "motdepasse";

        connection = connectPostgres(url, db, user, password);
    }

    private Optional<Drug> getRandomDrug() throws SQLException, ClassNotFoundException {
        if (null == connection) {
            connect();
        }

        var response = connection.prepareStatement("SELECT cip, prix FROM drugs4projet ORDER BY RANDOM() LIMIT 1").executeQuery();

        if (response.next()) {
            return Optional.of(new Drug(response.getInt("cip"), response.getDouble("prix")));
        }

        return Optional.empty();
    }

    private Name getRandomName() {
        return faker.name();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        var producer = new Producer();

        for(var i = 0; i < 10; i++)
            System.out.println(producer.getRandomDrug());
    }
}

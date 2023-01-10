package fr.uge.tp2;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;

import java.sql.Connection;
import java.sql.SQLException;

import static fr.uge.tp2.Utils.connectPostgres;

public class Main {

    private static void main(String[] args) throws SQLException, ClassNotFoundException {
        var url = "localhost:5432";
        var db = "postgres";
        var user = "postgres";
        var password = "motdepasse";

        try (var c = connectPostgres(url, db, user, password)) {
            System.out.println("Hello World!");
        } catch (Exception e) {
            System.out.println("Error "+e);
        }
    }
}
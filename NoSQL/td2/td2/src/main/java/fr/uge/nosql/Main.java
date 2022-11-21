package fr.uge.nosql;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.Objects;

public class Main {
    private JedisPool pool = new JedisPool(new JedisPoolConfig(),"localhost");

    static class Person {
        private Map<String, Integer> id;
        private Map<String, String> name;
        private Map<String, String> firstName;

        Person(int id, String name, String firstName) {
            this.id = Map.of("id", id);
            this.name = Map.of("name", name);
            this.firstName = Map.of("firstname", firstName);
        }
    }

    public static void main(String[] args) {
        JedisPool pool = new JedisPool(new JedisPoolConfig(),"localhost");
        var resources = pool.getResource();
        var p1 = new Person(1, "John", "Doe");
        var p2 = new Person(2, "Jane", "Dine");
        var p3 = new Person(3, "Pierre", "Feuille");
        System.out.println(resources.hgetAll("user:1000"));
        resources.hmset("user1", Map.of("John", "Doe"));
    }
}

/**
 * "org.postgresql.driver"
 * "jdbc: postgresql://localhost/dbname"
 * login
 * password
 */
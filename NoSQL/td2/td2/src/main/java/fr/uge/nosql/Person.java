package fr.uge.nosql;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

public class Person {
    private JedisPool pool = new JedisPool(new JedisPoolConfig(),"localhost");

    static class Personne {
        private Map<String, String> id;
        private Map<String, String> name;
        private Map<String, String> firstName;

        Personne(String name, String firstName) {
            this.name = Map.of("name", name);
            this.firstName = Map.of("firstname", firstName);
        }

        public void store(Jedis resources, String id) {
            try {
                resources.hset(id, firstName);
                resources.hset(id, name);
            } catch(Exception e) {
                throw e;
            }
            this.id = Map.of("id", id);
        }

        public void print(Jedis resources) {
            if (null != id) {
                System.out.println(resources.hgetAll(id.get("id")));
            }
        }
    }

    private void tests() {

    }

    public static void main(String[] args) {
        JedisPool pool = new JedisPool(new JedisPoolConfig(),"localhost");
        var resources = pool.getResource();

        var p1 = new Personne("John", "Doe");
        var p2 = new Personne("Jane", "Dine");
        var p3 = new Personne("Pierre", "Feuille");

        //System.out.println(resources.hgetAll("user:1000"));

        p1.store(resources, "user:1001");
        p2.store(resources, "user:1002");
        p3.store(resources, "user:1003");

        p1.print(resources);
        p2.print(resources);
        p3.print(resources);
    }
}

/**
 * "org.postgresql.driver"
 * "jdbc: postgresql://localhost/dbname"
 * login
 * password
 */
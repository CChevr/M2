package fr.uge.nosql;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class CacheManager {
    JedisPool pool;
    Connection connection;
    private final double sizeMax = 10;        // 1000
    private double serial;
    private final String hkey = "history";
    private final String ckey = "cache";

    private CacheManager(JedisPool pool, Connection connection) {
        this.pool = pool;
        this.connection = connection;
    }

    private double getNext() {
        return serial++;
    }

    private double getCurrent() {
        return serial;
    }

    private boolean updateHistory(Jedis resources, String member) {
        resources.zadd(hkey, Map.of(member, getNext()));

        if (sizeMax < resources.zcount(hkey, 0d, getCurrent())) {
            resources.zpopmin(hkey);
            return false;
        }

        return true;
    }

    public void get(String member) {
        var resources = pool.getResource();

        var present = updateHistory(resources, member);

        if(!present) {
            // -> requete
            // -> store
        } else {
            // -> cache
        }
    }

    public static CacheManager getCacheManager() throws SQLException, ClassNotFoundException {
        //var url = "sqletud.u-pem.fr";
        //var db = "cedric.chevreuil_db";

        var url = "localhost:5432";
        var db = "postgres";

        var pool = new JedisPool(new JedisPoolConfig(),"localhost");
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://"+url+"/"+db,
                "Cedric",
                "Motdepasse");

        return new CacheManager(pool, c);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        var cacheManager = getCacheManager();
    }
}
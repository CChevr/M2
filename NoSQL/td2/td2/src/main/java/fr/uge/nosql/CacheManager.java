package fr.uge.nosql;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class CacheManager {
    JedisPool pool;
    Connection connection;
    private final double sizeMax = 10;        // 1000
    private double serial;
    private final String hkey = "history";
    private final String ckey = "drug: ";

    private class Drug {
        private final int cip7;
        private final int cis;
        //private final String denom;

        Drug(int cip7, int cis) {
            this.cip7 = cip7;
            this.cis = cis;
            //this.denom =  denom;
        }

        @Override
        public String toString() {
            return "cip7 : "+cip7+"  cis : "+cis;
        }
    }

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

    private void updateHistory(Jedis resources, int cip7) {
        resources.zadd(hkey, Map.of(String.valueOf(cip7), getNext()));

        if (sizeMax < resources.zcount(hkey, 0d, getCurrent()))
            resources.zpopmin(hkey);
    }

    private boolean isInCache(int cip7) {
        try(var resource = pool.getResource()) {
            return (null == resource.zscore(hkey, String.valueOf(cip7)));
        }
    }

    private Optional<Drug> getDrugSQL(int cip7) throws SQLException {
        var res = connection.prepareStatement("SELECT cis, denom FROM bdpm_cis WHERE cis = "+cip7).executeQuery();

        if (res.next()) {
            return Optional.of(new Drug(cip7, res.getInt("cis")));
        }

        return Optional.empty();
    }

    private void storeDrugRedis(Drug drug) {
        try(var resource = pool.getResource()) {
            resource.hset(ckey + drug.cip7, Map.of("cis", String.valueOf(drug.cis)));
            updateHistory(resource, drug.cip7);
        }
    }

    private Optional<Drug> getDrugRedis(int cip7) {
        try(var resource = pool.getResource()) {
            var cis = resource.hget(ckey + cip7, "cis");
            //var denom = resources.hget(ckey+cip7, "denom");

            if (null == cis)
                return Optional.empty();

            try {
                var drug = Optional.of(new Drug(cip7, Integer.parseInt(cis)));
                updateHistory(resource, cip7);
                return drug;
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }

    public Optional<Drug> get(int cip7) throws SQLException {
        if (!isInCache(cip7)) {
            var drug = getDrugSQL(cip7);
            drug.ifPresent(this::storeDrugRedis);
            return drug;
        } else {
            return getDrugRedis(cip7);
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
        cacheManager.get(61266250);
    }
}
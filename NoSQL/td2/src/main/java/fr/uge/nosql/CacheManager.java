package fr.uge.nosql;

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
    private final double sizeMax = 3;        // 1000
    private final String hkey = "history";
    private final String ckey = "drug: ";

    private static class Drug {
        private final int cip7;
        private final int cis;
        private final String denom;

        Drug(int cip7, int cis, String denom) {
            this.cip7 = cip7;
            this.cis = cis;
            this.denom =  denom;
        }

        @Override
        public String toString() {
            return "cip7 : "+cip7+"  cis : "+cis+"   denom : "+denom;
        }
    }

    private CacheManager(JedisPool pool, Connection connection) {
        this.pool = pool;
        this.connection = connection;
    }

    private double getNext() {
        try(var resource = pool.getResource()) {
            return resource.incr("myCounter");
        }
    }

    private void updateHistory(int cip7) {
        String cip7ToDelete = null;
        var score = getNext();

        try(var resource = pool.getResource()) {
            resource.zadd(hkey, Map.of(String.valueOf(cip7), score));

            if (resource.zcount(hkey, "-inf", "+inf") > sizeMax) {
                var tuple = resource.zpopmin(hkey);
                cip7ToDelete = tuple.getElement();
            }
        }

        if (null != cip7ToDelete) {
            deleteDrugRedis(cip7ToDelete);
        }
    }

    private boolean isInCache(int cip7) {
        try(var resource = pool.getResource()) {
            return (null != resource.zscore(hkey, String.valueOf(cip7)));
        }
    }

    private Optional<Drug> getDrugSQL(int cip7) throws SQLException {
        var res = connection.prepareStatement("SELECT cis, denom FROM denorm_ciscip WHERE cip7 = "+cip7).executeQuery();

        if (res.next()) {
            return Optional.of(new Drug(cip7, res.getInt("cis"), res.getString("denom")));
        }

        return Optional.empty();
    }

    private void storeDrugRedis(Drug drug) {
        try(var resource = pool.getResource()) {
            resource.hset(ckey + drug.cip7, Map.of("cis", String.valueOf(drug.cis)));
            resource.hset(ckey + drug.cip7, Map.of("denom", drug.denom));
            updateHistory(drug.cip7);
        }
    }

    private void deleteDrugRedis(String cip7) {
        try(var resource = pool.getResource()) {
            resource.hdel(ckey+cip7, "cis");
            resource.hdel(ckey+cip7, "denom");
        }
    }

    private Optional<Drug> getDrugRedis(int cip7) {
        String cis = null;
        String denom = null;

        try(var resource = pool.getResource()) {
            cis = resource.hget(ckey+cip7, "cis");
            denom = resource.hget(ckey+cip7, "denom");
        }

        if (null == cis) {
            return Optional.empty();
        }

        try {
            var drug = Optional.of(new Drug(cip7, Integer.parseInt(cis), denom));
            updateHistory(cip7);
            return drug;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Drug> get(int cip7) throws SQLException {
        if (isInCache(cip7)) {
            return getDrugRedis(cip7);
        } else {
            var drug = getDrugSQL(cip7);
            drug.ifPresent(this::storeDrugRedis);
            return drug;
        }
    }

    public static CacheManager getCacheManager() throws SQLException, ClassNotFoundException {
        var url = "localhost:5432";
        var db = "postgres";

        var pool = new JedisPool(new JedisPoolConfig(),"localhost");
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://"+url+"/"+db,
                "Cedric",
                "Motdepasse");

        return new CacheManager(pool, c);
    }

    /*
    3022629 | 60217542 | W771
    3018612 | 65661049 | W146
    3021006 | 69190178 | W580
    3117783 | 69232606 | PERCUTALGINE, gel
    4949729 | 60002283 | ANASTROZOLE ACCORD 1 mg, comprim├® pellicul├®
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        var cacheManager = getCacheManager();

        var d1 = cacheManager.get(3022629);
        var d2 = cacheManager.get(3018612);
        var d3 = cacheManager.get(3021006);

        d1.ifPresent(System.out::println);
        d2.ifPresent(System.out::println);
        d3.ifPresent(System.out::println);

        var d4 = cacheManager.get(3117783);
        d4.ifPresent(System.out::println);

        var d4_bis = cacheManager.get(4949729);
        d4_bis.ifPresent(System.out::println);
    }
}
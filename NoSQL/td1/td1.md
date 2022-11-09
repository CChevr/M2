# Denormalization and query execution performance on RDBMS

## 1 Dataset
## 2 Transform and load the data into PostgreSQL

Création des tables sql
```sql
CREATE TABLE bdpm_cis (
    cis integer NOT NULL,
    denom character varying(255),
    forme character varying(255),
    voie character varying(255),
    statadm character varying(100),
    typproc character varying(255),
    etacom character varying(100),
    datamm date,
    labotit character varying(255)
);

CREATE TABLE bdpm_ciscip2 (
    cip7 integer NOT NULL,
    cip13 bigint,
    cis integer,
    prese character varying(255),
    statadm character varying(100),
    etacom character varying(100),
    datedecl date,
    agrcol character(3),
    prixe double precision,
    liste integer,
    taux integer,
    fprixe double precision,
    disp double precision
);
```

Import des données
```sql
\i cis.sql
\i ciscip.sql
```

sauvegarde des données
```shell
pg_dump -h sqletud.u-pem.fr cedric.chevreuil_db > saveTD1.sql
```

## 3 Querying

We would like to study the impact of various approaches on query performance. We are interested in two
realistic queries. The first one is not selective since it retrieves over 2000 records from the database. The
second one is highly selective since it is retrieving a single record:

```sql
select cip7, cc.cis, denom from bdpm_cis as c join bdpm_ciscip2 as cc on cc.cis=c.cis where cip7<3000000;
select cip7, cc.cis, denom from bdpm_cis as c join bdpm_ciscip2 as cc on cc.cis=c.cis where cip7=3000000;
```

### 3.1 No denormalization, no indexes
Using explain analysis, note the performance of each query. Execute several times the Q1, do you see any difference in terms of execution time? Why would it be fair to consider an average of several runs (3 to 5)
starting from the 2nd run?
Q1:
```sql
explain analyze select cip7, cc.cis, denom from bdpm_cis as c join bdpm_ciscip2 as cc on cc.cis=c.cis where cip7<3000000;
```
```
                                                        QUERY PLAN                                                         
---------------------------------------------------------------------------------------------------------------------------
 Hash Join  (cost=430.54..1202.00 rows=5191 width=524) (actual time=116.448..166.702 rows=2474 loops=1)
   Hash Cond: (cc.cis = c.cis)
   ->  Seq Scan on bdpm_ciscip2 cc  (cost=0.00..587.25 rows=1260 width=8) (actual time=0.062..34.551 rows=2476 loops=1)
         Filter: (cip7 < 3000000)
         Rows Removed by Filter: 18321
   ->  Hash  (cost=420.24..420.24 rows=824 width=520) (actual time=116.341..116.341 rows=15696 loops=1)
         Buckets: 16384 (originally 1024)  Batches: 1 (originally 1)  Memory Usage: 1550kB
         ->  Seq Scan on bdpm_cis c  (cost=0.00..420.24 rows=824 width=520) (actual time=0.027..66.856 rows=15696 loops=1)
 Planning time: 0.148 ms
 Execution time: 169.772 ms
(10 lignes)
```


Q2:
```sql
explain analyze select cip7, cc.cis, denom from bdpm_cis as c join bdpm_ciscip2 as cc on cc.cis=c.cis where cip7=3000000;
```
```
                                                    QUERY PLAN                                                    
------------------------------------------------------------------------------------------------------------------
 Hash Join  (cost=430.54..1020.57 rows=78 width=524) (actual time=7.425..7.425 rows=0 loops=1)
   Hash Cond: (cc.cis = c.cis)
   ->  Seq Scan on bdpm_ciscip2 cc  (cost=0.00..587.25 rows=19 width=8) (actual time=7.421..7.421 rows=0 loops=1)
         Filter: (cip7 = 3000000)
         Rows Removed by Filter: 20797
   ->  Hash  (cost=420.24..420.24 rows=824 width=520) (never executed)
         ->  Seq Scan on bdpm_cis c  (cost=0.00..420.24 rows=824 width=520) (never executed)
 Planning time: 0.125 ms
 Execution time: 7.473 ms
(9 lignes)
```
On peut remarquer que la première exécution (Cold run) de la requête est plus longue que la seconde exécution(Cold run). cela est du au fait que le résultat de la requête est stocké en cache

## 3.2 No denormalization, indexes

On choisi de créer un index sur la colonne 'cis' des deux tables

```sql
CREATE INDEX cis ON bdpm_cis(cis);
```
```
                                                             QUERY PLAN                                                             
------------------------------------------------------------------------------------------------------------------------------------
 Merge Join  (cost=1242.48..11505.50 rows=544023 width=524) (actual time=22.813..167.785 rows=2474 loops=1)
   Merge Cond: (c.cis = cc.cis)
   ->  Index Scan using cis on bdpm_cis c  (cost=0.29..2063.72 rows=15696 width=520) (actual time=0.039..77.355 rows=15689 loops=1)
   ->  Sort  (cost=1242.19..1259.52 rows=6932 width=8) (actual time=22.483..26.133 rows=2476 loops=1)
         Sort Key: cc.cis
         Sort Method: quicksort  Memory: 213kB
         ->  Seq Scan on bdpm_ciscip2 cc  (cost=0.00..799.96 rows=6932 width=8) (actual time=0.020..14.717 rows=2476 loops=1)
               Filter: (cip7 < 3000000)
               Rows Removed by Filter: 18321
 Planning time: 0.282 ms
 Execution time: 174.249 ms
(11 lignes)
```
```
                                                             QUERY PLAN                                                             
------------------------------------------------------------------------------------------------------------------------------------
 Merge Join  (cost=1242.48..11505.50 rows=544023 width=524) (actual time=10.869..156.830 rows=2474 loops=1)
   Merge Cond: (c.cis = cc.cis)
   ->  Index Scan using cis on bdpm_cis c  (cost=0.29..2063.72 rows=15696 width=520) (actual time=0.024..90.383 rows=15689 loops=1)
   ->  Sort  (cost=1242.19..1259.52 rows=6932 width=8) (actual time=10.756..14.398 rows=2476 loops=1)
         Sort Key: cc.cis
         Sort Method: quicksort  Memory: 213kB
         ->  Seq Scan on bdpm_ciscip2 cc  (cost=0.00..799.96 rows=6932 width=8) (actual time=0.009..6.889 rows=2476 loops=1)
               Filter: (cip7 < 3000000)
               Rows Removed by Filter: 18321
 Planning time: 0.128 ms
 Execution time: 159.762 ms
(11 lignes)
```

```sql
CREATE INDEX cis_ciscip ON bdpm_ciscip2(cis);
```
```
                                                       QUERY PLAN                                                        
-------------------------------------------------------------------------------------------------------------------------
 Hash Join  (cost=801.26..1687.28 rows=8162 width=524) (actual time=2.837..2.837 rows=0 loops=1)
   Hash Cond: (c.cis = cc.cis)
   ->  Seq Scan on bdpm_cis c  (cost=0.00..568.96 rows=15696 width=520) (actual time=0.011..0.011 rows=1 loops=1)
   ->  Hash  (cost=799.96..799.96 rows=104 width=8) (actual time=2.817..2.817 rows=0 loops=1)
         Buckets: 1024  Batches: 1  Memory Usage: 8kB
         ->  Seq Scan on bdpm_ciscip2 cc  (cost=0.00..799.96 rows=104 width=8) (actual time=2.812..2.812 rows=0 loops=1)
               Filter: (cip7 = 3000000)
               Rows Removed by Filter: 20797
 Planning time: 0.139 ms
 Execution time: 2.876 ms
(10 lignes)
```
```
                                                       QUERY PLAN                                                        
-------------------------------------------------------------------------------------------------------------------------
 Hash Join  (cost=801.26..1687.28 rows=8162 width=524) (actual time=2.653..2.653 rows=0 loops=1)
   Hash Cond: (c.cis = cc.cis)
   ->  Seq Scan on bdpm_cis c  (cost=0.00..568.96 rows=15696 width=520) (actual time=0.007..0.007 rows=1 loops=1)
   ->  Hash  (cost=799.96..799.96 rows=104 width=8) (actual time=2.638..2.638 rows=0 loops=1)
         Buckets: 1024  Batches: 1  Memory Usage: 8kB
         ->  Seq Scan on bdpm_ciscip2 cc  (cost=0.00..799.96 rows=104 width=8) (actual time=2.633..2.633 rows=0 loops=1)
               Filter: (cip7 = 3000000)
               Rows Removed by Filter: 20797
 Planning time: 0.127 ms
 Execution time: 2.710 ms
(10 lignes)
```
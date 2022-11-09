# Denormalization and query execution performance on RDBMS

## Dataset

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
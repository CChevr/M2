package fr.uge.jee.hibernate.employees;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUtils {
    static final EntityManagerFactory ENTITY_MANAGER_FACTORY
            = Persistence.createEntityManagerFactory("main-persistence-unit");

    static EntityManagerFactory getEntityManagerFactory(){
        return ENTITY_MANAGER_FACTORY;
    }
}

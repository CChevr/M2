package fr.uge.jee.hibernate.employees.repositories;

import fr.uge.jee.hibernate.employees.models.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;
import java.util.function.Function;

public class PersistenceUtils {
    static final EntityManagerFactory ENTITY_MANAGER_FACTORY
            = Persistence.createEntityManagerFactory("main-persistence-unit");

    static EntityManagerFactory getEntityManagerFactory(){
        return ENTITY_MANAGER_FACTORY;
    }

    public static void inTransaction(Consumer<EntityManager> consumer){
        var em = getEntityManagerFactory().createEntityManager();
        var tx = em.getTransaction();

        try {
            tx.begin();
            consumer.accept(em);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public static <T> T inTransaction(Function<EntityManager,T> action){
        var em = getEntityManagerFactory().createEntityManager();
        var tx = em.getTransaction();

        try {
            tx.begin();
            var res = action.apply(em);
            tx.commit();
            return res;
        } catch(Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

}

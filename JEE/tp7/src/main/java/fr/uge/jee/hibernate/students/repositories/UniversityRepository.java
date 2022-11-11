package fr.uge.jee.hibernate.students.repositories;

import fr.uge.jee.hibernate.students.models.University;

import javax.persistence.EntityManager;
import java.util.function.Consumer;
import java.util.function.Function;

public class UniversityRepository {
    /**
     * Create a university with the given information
     * @param name name of the university
     * @return the id of the newly created university
     */
    public long create(String name) {
        Function<EntityManager, University> function = em -> {
            var university = new University(name);
            em.persist(university);
            return university;
        };

        return PersistenceUtils.inTransaction(function).getId();
    }

    /**
     * Remove the university with the given id from the DB
     * @param id id of the university to remove
     * @return true if the removal was successful
     */
    public boolean delete(long id) {
        Consumer<EntityManager> consumer = em -> {
            var university = em.find(University.class, id);
            if (null == university) {
                throw new IllegalArgumentException("wrong id");
            }
            em.remove(university);
        };

        try {
            PersistenceUtils.inTransaction(consumer);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
